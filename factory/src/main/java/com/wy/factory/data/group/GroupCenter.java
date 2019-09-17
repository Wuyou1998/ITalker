package com.wy.factory.data.group;

import com.wy.factory.model.card.GroupCard;
import com.wy.factory.model.card.GroupMemberCard;

/* 名称: ITalker.com.wy.factory.data.group.GroupCenter
 * 用户: _VIEW
 * 时间: 2019/9/17,17:24
 * 描述: 群聊中心的接口定义
 */
public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}
