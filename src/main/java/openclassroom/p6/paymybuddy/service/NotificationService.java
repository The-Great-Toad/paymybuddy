package openclassroom.p6.paymybuddy.service;

import openclassroom.p6.paymybuddy.domain.Notification;
import openclassroom.p6.paymybuddy.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Iterable<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotification(Integer id) {
        return notificationRepository.findById(id);
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
}
