package com.wy.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wy.common.factory.data.DataSource;
import com.wy.factory.Factory;
import com.wy.factory.R;
import com.wy.factory.model.api.RspModel;
import com.wy.factory.model.api.group.GroupCreateModel;
import com.wy.factory.model.card.GroupCard;
import com.wy.factory.model.db.Group;
import com.wy.factory.model.db.Group_Table;
import com.wy.factory.model.db.User;
import com.wy.factory.net.Network;
import com.wy.factory.net.RemoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* 名称: ITalker.com.wy.factory.data.helper.GroupHelper
 * 用户: _VIEW
 * 时间: 2019/9/17,17:38
 * 描述: 群聊的简单辅助工具类
 */
public class GroupHelper {
    //查询群的信息，先本地后网络
    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null)
            group = findFromNet(groupId);
        return group;
    }

    //从本地找
    public static Group findFromLocal(String groupId) {
        return SQLite.select().from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();
    }

    //从网络找
    public static Group findFromNet(String groupId) {
        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<GroupCard>> response = remoteService.groupFind(groupId).execute();
            GroupCard card = response.body().getResult();
            if (card != null) {
                // 数据库的存储并通知
                Factory.getGroupCenter().dispatch(card);
                User user = UserHelper.search(card.getOwnerId());
                if (user != null) {
                    return card.build(user);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //群的创建
    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        RemoteService service = Network.remote();
        service.groupCreate(model).enqueue(new Callback<RspModel<GroupCard>>() {
            @Override
            public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
                RspModel<GroupCard> rspModel = response.body();
                if (rspModel.success()) {
                    GroupCard groupCard = rspModel.getResult();
                    //唤起进行保存的操作
                    Factory.getGroupCenter().dispatch(groupCard);
                    // 返回数据
                    callback.onDataLoad(groupCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    public static Call search(String content, final DataSource.Callback<List<GroupCard>> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<List<GroupCard>>> call = service.groupSearch(content);

        call.enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {
                RspModel<List<GroupCard>> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoad(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        //把当前调度者返回
        return call;
    }
}
