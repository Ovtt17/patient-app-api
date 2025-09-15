package com.patientapp.notificationservice.notification.service;

import com.patientapp.notificationservice.notification.entity.Notification;
import com.patientapp.notificationservice.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    @Transactional
    public void save(Notification notification) {
        repository.save(notification);
    }
}
