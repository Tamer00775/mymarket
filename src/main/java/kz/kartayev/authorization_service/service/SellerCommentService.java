package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.adapter.SellerCommentAdapter;
import kz.kartayev.authorization_service.commons.dto.SellerCommentDto;
import kz.kartayev.authorization_service.entity.SellerComment;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCustomer;
import kz.kartayev.authorization_service.repository.SellerCommentRepository;
import kz.kartayev.authorization_service.util.error.FLCException;
import kz.kartayev.authorization_service.util.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SellerCommentService {
  private final SellerCommentRepository sellerCommentRepository;
  private final CustomerService customerService;

  public Page<SellerCommentDto> findAllByCustomer(Long customerId, Pageable pageable) {
    Optional<UserCustomer> optionalUserCustomer = customerService.findById(customerId);
    if (optionalUserCustomer.isPresent()) {
      Page<SellerComment> sellerComments = sellerCommentRepository.findAllByUserCustomer(optionalUserCustomer.get(), pageable);
      return sellerComments.map(SellerCommentAdapter::toDto);
    }
    throw new FLCException("", "Продавец не найден", "");
  }

  public Integer calculateRatingOfSeller(UserCustomer userCustomer) {
    return sellerCommentRepository.findAllByUserCustomer(userCustomer, Pageable.unpaged()).getContent().size();
  }

  @Transactional
  public void addComment(Long customerId, SellerCommentDto sellerCommentDto) {
    User user = SecurityUtils.getCurrentUser();
    SellerComment sellerComment = save(sellerCommentDto, user, customerId);
    UserCustomer userCustomer = sellerComment.getUserCustomer();
    userCustomer.setAvgRate(calculateAvg(userCustomer, sellerComment.getRating()));
    customerService.save(userCustomer);
  }
  @Transactional
  public void deleteComment(Long commentId) {
    sellerCommentRepository.deleteById(commentId);
  }

  public SellerComment save(SellerCommentDto sellerCommentDto, User user, Long customerId) {
    SellerComment sellerComment = new SellerComment();
    sellerComment.setCommentText(sellerCommentDto.getComment());
    sellerComment.setRating(sellerCommentDto.getRate());
    sellerComment.setUserCustomer(customerService.findById(customerId).get());
    sellerComment.setCreatedDate(LocalDateTime.now());
    sellerComment.setAuthor(buildFullname(user));
    return sellerCommentRepository.save(sellerComment);
  }

  private String buildFullname(User user) {
    return user.getFirstName() + " " + user.getLastName();
  }

  private Double calculateAvg(UserCustomer userCustomer, Integer rating) {
    int numRatings = calculateRatingOfSeller(userCustomer);
    if (numRatings == 0) {
      numRatings = 1;
    }
    double avg = userCustomer.getAvgRate() == null ? 0 : userCustomer.getAvgRate();
    return (avg * numRatings + rating) / (numRatings);
  }
}
