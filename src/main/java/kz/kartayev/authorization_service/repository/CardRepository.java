package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.UserCardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<UserCardInfo, Long> {
  Optional<UserCardInfo> findUserCardInfoByCardNumber(String cardNum);
}
