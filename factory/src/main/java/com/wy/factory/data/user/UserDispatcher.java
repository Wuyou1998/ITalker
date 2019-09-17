package com.wy.factory.data.user;

import android.text.TextUtils;

import com.wy.factory.data.helper.DbHelper;
import com.wy.factory.model.card.UserCard;
import com.wy.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* 名称: ITalker.com.wy.factory.data.user.UserDispatcher
 * 用户: _VIEW
 * 时间: 2019/9/17,17:01
 * 描述: 对UserCard的处理
 */
public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    //单线程池，处理卡片，一个个的进行消息处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static UserCenter getInstance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new UserDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0) {
            return;
        }
        //交给单线程池去处理
        executor.execute(new UserCardHandler(cards));
    }

    /**
     * 线程调度时会触发run方法
     */
    private class UserCardHandler implements Runnable {
        private final UserCard[] cards;

        UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            //当被线程调度的时候触发
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                //过滤空card
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                //添加
                users.add(card.build());
            }
            //进行数据库存储，并分发通知，异步的操作
            DbHelper.save(User.class, users.toArray(new User[0]));
        }
    }
}
