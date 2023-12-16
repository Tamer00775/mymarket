package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTransactionHistoryRepository extends JpaRepository<UserTransactionHistory, Long> {

  List<UserTransactionHistory> findAllByUser(User user);
}
