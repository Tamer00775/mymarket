package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.CategoryDto;
import kz.kartayev.authorization_service.entity.Category;
import kz.kartayev.authorization_service.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @GetMapping
  @ApiOperation("Список всех категории в системе")
  public ResponseEntity<List<Category>> findAll() {
    return ResponseEntity.ok(categoryService.findAll());
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
  public ResponseEntity<HttpStatus> addCategory(CategoryDto categoryDto) {
    categoryService.save(categoryDto);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
  public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long id) {
    deleteCategory(id);
    return ResponseEntity.ok(HttpStatus.OK);
  }
}
