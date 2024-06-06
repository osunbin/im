package com.bin.im.server.repositories.model.contacts;

public class ContactsUnreadModel extends BaseContactsModel{
    private boolean isDel;
    private int unreadCount;

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
