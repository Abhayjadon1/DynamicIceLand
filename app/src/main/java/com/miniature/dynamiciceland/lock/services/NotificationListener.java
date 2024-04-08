package com.miniature.dynamiciceland.lock.services;

import com.miniature.dynamiciceland.lock.entity.Notification;

public interface NotificationListener {
    void onItemClicked(Notification notification);

    void onItemClicked(Notification notification, int i);
}
