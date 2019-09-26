package com.wy.factory.model.api.group;

import java.util.Set;

/**
 * @author qiujuer Email:qiujuer@live.cn
 */
public class GroupMemberAddModel {
    private Set<String> users;

    public GroupMemberAddModel(Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
