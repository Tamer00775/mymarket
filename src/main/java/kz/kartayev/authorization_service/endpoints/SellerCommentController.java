package kz.kartayev.authorization_service.endpoints;

import kz.kartayev.authorization_service.commons.dto.SellerCommentDto;
import kz.kartayev.authorization_service.entity.SellerComment;
import kz.kartayev.authorization_service.service.SellerCommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class SellerCommentController {
  private final SellerCommentService sellerCommentService;

  @GetMapping("/{sellerId}")
  public ResponseEntity<Page<SellerCommentDto>> findAllCommentByCustomer(@RequestParam(value = "size", defaultValue = "10") int size,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "sort", defaultValue = "createdDate") String sort,
                                                                         @PathVariable Long sellerId) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    return ResponseEntity.ok(sellerCommentService.findAllByCustomer(sellerId, pageable));
  }

  @PostMapping("/{sellerId}")
  public ResponseEntity<String> addComment(@PathVariable Long sellerId, @RequestBody SellerCommentDto sellerCommentDto) {
    sellerCommentService.addComment(sellerId, sellerCommentDto);
    return ResponseEntity.ok().body("OK");
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<String> deleteCommentById(@PathVariable Long commentId) {
    sellerCommentService.deleteComment(commentId);
    return ResponseEntity.ok().body("DELETED");
  }
}
