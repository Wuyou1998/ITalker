package com.wy.factory.data.message;

import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wy.common.Common;
import com.wy.common.app.Application;
import com.wy.common.utils.PicturesCompressor;
import com.wy.common.utils.StreamUtil;
import com.wy.factory.Factory;
import com.wy.factory.model.api.RspModel;
import com.wy.factory.model.api.message.MsgCreateModel;
import com.wy.factory.model.card.MessageCard;
import com.wy.factory.model.db.Message;
import com.wy.factory.model.db.Message_Table;
import com.wy.factory.net.Network;
import com.wy.factory.net.RemoteService;
import com.wy.factory.net.UploadHelper;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* 名称: ITalker.com.wy.factory.data.message.MessageHelper
 * 用户: _VIEW
 * 时间: 2019/9/17,17:46
 * 描述: 消息 辅助工具类
 */
public class MessageHelper {
    //从本地找消息
    public static Message findFromLocal(String id) {

        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    //发送是一步进行的
    public static void push(final MsgCreateModel msgCreateModel) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                //成功发送：如果是一个已经发送过的消息，则不能重新发送
                //正在发送状态：如果是一个消息正在发送，则不能重新发送
                Message message = findFromLocal(msgCreateModel.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED)
                    return;


                //在发送时需要通知界面刷新状态，Card
                final MessageCard card = msgCreateModel.buildCard();
                Factory.getMessageCenter().dispatch(card);

                // 文件类型的（图片，语音，文件），需要先上传后才发送
                if (card.getType() != Message.TYPE_STR) {
                    //非文字类型
                    if (!card.getContent().startsWith(Common.ALI_END_POINT)) {
                        //没有上传服务器，还是手机中的路径
                        String content;
                        switch (card.getType()) {
                            case Message.TYPE_PIC:
                                content = upLoadPic(card.getContent());
                                break;
                            case Message.TYPE_AUDIO:
                                content = upLoadAudio(card.getContent());
                                break;
                            default:
                                content = "";
                                break;
                        }
                        if (TextUtils.isEmpty(content)) {
                            //失败
                            card.setStatus(Message.STATUS_FAILED);
                            Factory.getMessageCenter().dispatch(card);
                        }
                        //成功则用网络路径替换本地路径
                        card.setContent(content);
                        Factory.getMessageCenter().dispatch(card);
                        //卡片内容变了，上传服务器用的是msgCreateModel，所以msgCreateModel也要改
                        msgCreateModel.refreshByCard();
                    }
                }

                //直接发送
                RemoteService service = Network.remote();
                service.msgPush(msgCreateModel).enqueue(new Callback<RspModel<MessageCard>>() {
                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (card != null) {
                                //成功的一次调度
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            //检查账户是否异常
                            Factory.decodeRspCode(rspModel, null);
                            //走失败流程
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {
                        //通知失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    //上传图片
    private static String upLoadPic(String content) {
        File file = null;
        try {
            //通过glide的缓存区间解决了图片外部权限的问题
            file = Glide.with(Application.getInstance()).load(content)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            //进行压缩
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            String tempFile = String.format("%s/image/Cache_%s.png", cacheDir, SystemClock.uptimeMillis());
            try {
                // 压缩工具类
                if (PicturesCompressor.compressImage(file.getAbsolutePath(), tempFile,
                        Common.MAX_UPLOAD_IMAGE_LENGTH)) {
                    //上传
                    String ossPath = UploadHelper.uploadImage(tempFile);
                    //清理缓存
                    StreamUtil.delete(tempFile);
                    return ossPath;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    //上传语音
    private static String upLoadAudio(String content) {
        // 上传语音
        File file = new File(content);
        if (!file.exists() || file.length() <= 0)
            return null;
        // 上传并返回
        return UploadHelper.uploadAudio(content);
    }

    /**
     * 查询一个消息，这个消息是一个群中的最后一条消息
     *
     * @param groupId 群id
     * @return 群众聊天的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    /**
     * 查询一个消息，这个消息是一个user的最后一条消息
     *
     * @param userId userId
     * @return 一个user的最后一条消息
     */

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(userId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }
}
