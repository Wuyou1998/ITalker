package com.wy.factory.data.message;

import com.wy.factory.model.card.MessageCard;

/* 名称: ITalker.com.wy.factory.data.message.MessageCenter
 * 用户: _VIEW
 * 时间: 2019/9/17,17:16
 * 描述: 信息中心的基本定义，进行消息卡片的消费
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
