package com.wy.factory.data.user;

import com.wy.factory.model.card.UserCard;

/* 名称: ITalker.com.wy.factory.data.user.UserCenter
 * 用户: _VIEW
 * 时间: 2019/9/17,16:50
 * 描述: 用戶中心的基本定义
 */
public interface UserCenter {
    // 分发处理一堆用户卡片的信息，并更新到数据库
    void dispatch(UserCard... cards);
}
