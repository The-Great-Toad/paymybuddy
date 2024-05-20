package openclassroom.p6.paymybuddy.repository;

import openclassroom.p6.paymybuddy.domain.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {
}
