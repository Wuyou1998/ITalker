package com.wy.factory.data.message;

import com.wy.common.factory.data.DbDataSource;
import com.wy.factory.model.db.Message;

/* 名称: ITalker.com.wy.factory.data.message.MessageDataSource
 * 用户: _VIEW
 * 时间: 2019/9/22,23:33
 * 描述: 消息的数据源定义，他的实现是：MessageRepository,MessageGroupRepository
 *      关注的对象是Message表
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
