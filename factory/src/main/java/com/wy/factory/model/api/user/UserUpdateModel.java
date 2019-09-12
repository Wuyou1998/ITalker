package com.wy.factory.model.api.user;

/**
 * 用户更新信息所使用的Model
 */
public class UserUpdateModel {
    private String name;
    private String avatar;
    private String description;
    private int sex;

    public UserUpdateModel(String name, String avatar, String desc, int sex) {
        this.name = name;
        this.avatar = avatar;
        this.description = desc;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserUpdateModel{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", desc='" + description + '\'' +
                ", sex=" + sex +
                '}';
    }
}
