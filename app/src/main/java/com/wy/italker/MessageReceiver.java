package com.wy.italker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.wy.factory.Factory;
import com.wy.factory.data.helper.AccountHelper;
import com.wy.factory.persistence.Account;

/* 名称: ITalker.com.wy.italker.MessageReceiver
 * 用户: _VIEW
 * 时间: 2019/9/11,15:33
 * 描述: 个推消息处理
 */
public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MessageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null)
            return;
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                //当Id初始化的时候,获取设备ID
                Log.e(TAG, "GET_CLIENTID: " + bundle.toString());
                onClientInit(bundle.getString("clientid"));
                break;
            case PushConsts.GET_MSG_DATA:
                //常规消息送达
                Log.e(TAG, "GET_MSG_DATA: " + bundle.toString());
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String message = new String(payload);
                    onMessageArrived(message);
                }
            default:
                Log.e(TAG, "OTHERS: " + bundle.toString());
                break;
        }
    }

    /**
     * 当Id初始化的时候
     *
     * @param cid
     */
    private void onClientInit(String cid) {
        //设置设备ID
        Account.setPushId(cid);
        if (Account.isLogin()) {
            //账户登录状态，进行一次PushId绑定，没有登录是不能绑定PushId的
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息到达时
     *
     * @param message 新消息
     */
    private void onMessageArrived(String message) {
        //交由Factory处理
        Factory.dispatchPush(message);
    }
}
