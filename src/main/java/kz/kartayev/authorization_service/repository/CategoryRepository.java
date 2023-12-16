package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findCategoryByCategoryName(String categoryName);
}
