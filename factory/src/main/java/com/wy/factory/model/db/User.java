package com.wy.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;

import java.util.Date;
import java.util.Objects;

/* 名称: ITalker.com.wy.factory.model.db.User
 * 用户: _VIEW
 * 时间: 2019/9/9,17:07
 * 描述: bean user
 */
public class User {
    // 主键
    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String desc;
    @Column
    private int sex = 0;

    // 我对某人的备注信息，也应该写入到数据库中
    @Column
    private String alias;

    // 用户关注人的数量
    @Column
    private int follows;

    // 用户粉丝的数量
    @Column
    private int following;

    // 我与当前User的关系状态，是否已经关注了这个人
    @Column
    private boolean isFollow;

    // 时间字段
    @Column
    private Date modifyAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return sex == user.sex
                && follows == user.follows
                && following == user.following
                && isFollow == user.isFollow
                && Objects.equals(id, user.id)
                && Objects.equals(name, user.name)
                && Objects.equals(phone, user.phone)
                && Objects.equals(portrait, user.portrait)
                && Objects.equals(desc, user.desc)
                && Objects.equals(alias, user.alias)
                && Objects.equals(modifyAt, user.modifyAt);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
