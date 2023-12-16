package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.entity.Product;
import kz.kartayev.authorization_service.entity.ProductImages;
import kz.kartayev.authorization_service.repository.ProductImagesRepository;
import kz.kartayev.authorization_service.util.ImageUtil;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PhotoService {
  private final ProductImagesRepository productImagesRepository;
  private final ProductCommonService productCommonService;

  @Transactional
  public void savePhoto(Long productId, byte[] bytes) {
    ProductImages productImages = new ProductImages();
    Optional<Product> productOptional = productCommonService.findById(productId);
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      productImages.setProduct(product);
      productImages.setImageData(ImageUtil.compressImage(bytes));
      productImagesRepository.save(productImages);
    }
  }

  public List<byte[]> findAllPhotosByProductId(Long productId) {
    Optional<Product> productOptional = productCommonService.findById(productId);
    if (productOptional.isPresent()) {
      Product product = productOptional.get();
      List<ProductImages> productImages = productImagesRepository.findAllByProduct(product);
      return productImages.stream().map(ProductImages::getImageData)
              .map(ImageUtil::decompressImage).collect(Collectors.toList());
    }
    throw new FLCException("Photo", "Продукт не найден", "");
  }

  @Transactional
  public void deleteById(Long id) {
    log.info("start delete images of product_id {}", id);
    productImagesRepository.deleteByProduct(productCommonService.findById(id).get());
  }

}
