package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.ImageDto;
import kz.kartayev.authorization_service.entity.ImageResponse;
import kz.kartayev.authorization_service.entity.ProductImages;
import kz.kartayev.authorization_service.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/image")
@ApiModel(description = "Фотография продукта")
@Api(tags = "PhotoController", description = "API для работы с фотографиями")
public class ImageController {
  private final PhotoService photoService;

  @PostMapping("/{productId}")
  @ApiOperation("Создании фотографии по id продукта")
  public ResponseEntity<String> addImageToProduct(@PathVariable("productId") Long productId,
                                                  @RequestParam("file") MultipartFile file) {
    try {
      byte[] bytes = file.getBytes();
      photoService.savePhoto(productId, bytes);
      return ResponseEntity.ok().body("Загрузка прошла успешна!");
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body("Ошибка при загрузке фотографии");
    }
  }

  @GetMapping("/{productId}")
  @ApiOperation("Получение фотографии по id продукта")
  public ResponseEntity<byte[]> findPhotosByProductId(@PathVariable("productId") Long productId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    List<byte[]> productImages = photoService.findAllPhotosByProductId(productId);
    return new ResponseEntity<>(productImages.get(0),
            headers,
            HttpStatus.OK);
  }

  @DeleteMapping("/{productId}")
  @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'SUPERADMIN')")
  @ApiOperation("Удаление фотографии по id продукта")
  public void deleteById(@PathVariable Long productId) {
    photoService.deleteById(productId);
  }

  // @GetMapping("/photos/{id}")
  // public ResponseEntity<String> findPhotosByProductId2(@PathVariable("id") Long productId) {
  //   List<byte[]> images = photoService.findAllPhotosByProductId(productId);
//
  //   String concatenatedImages = images.stream()
  //           .map(Base64Utils::encodeToString)
  //           .collect(Collectors.joining(";"));
//
  //   HttpHeaders headers = new HttpHeaders();
  //   headers.setContentType(MediaType.IMAGE_JPEG);
//
  //   return new ResponseEntity<>(concatenatedImages, headers, HttpStatus.OK);
  // }
}
