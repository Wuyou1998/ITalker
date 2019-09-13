package com.wy.factory.model.card;

import com.wy.factory.model.db.Group;
import com.wy.factory.model.db.User;

import java.util.Date;

/* 名称: ITalker.com.wy.factory.model.card.GroupCard
 * 用户: _VIEW
 * 时间: 2019/9/13,22:49
 * 描述: 群信息
 */
public class GroupCard {
    private String id;
    private String name;
    private String description;
    private String picture;
    private String ownerId;
    private int notifyLevel;
    private Date joinAt;
    private Date modifyAt;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public Date getJoinAt() {
        return joinAt;
    }

    public void setJoinAt(Date joinAt) {
        this.joinAt = joinAt;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    /**
     * 把一个群的信息，build为一个群Model
     * 由于卡片中有创建者的Id，但是没有创建者这个人的Model；
     * 所以Model需求在外部准备好传递进来
     *
     * @param owner 创建者
     * @return 群信息
     */
    public Group build(User owner) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        group.setDescription(description);
        group.setPicture(picture);
        group.setNotifyLevel(notifyLevel);
        group.setJoinAt(joinAt);
        group.setModifyAt(modifyAt);
        group.setOwner(owner);
        return group;
    }
}
