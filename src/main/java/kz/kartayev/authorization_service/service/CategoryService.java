package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.dto.CategoryDto;
import kz.kartayev.authorization_service.entity.Category;
import kz.kartayev.authorization_service.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  public void save(CategoryDto categoryDto) {
    Category category = new Category();
    category.setCategoryName(category.getCategoryName());
    categoryRepository.save(category);
  }

  public void delete(Long id) {
    categoryRepository.deleteById(id);
  }
}
