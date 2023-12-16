package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.SellerComment;
import kz.kartayev.authorization_service.entity.UserCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerCommentRepository extends JpaRepository<SellerComment, Long> {
  Page<SellerComment> findAllByUserCustomer(UserCustomer userCustomer, Pageable pageable);

}
