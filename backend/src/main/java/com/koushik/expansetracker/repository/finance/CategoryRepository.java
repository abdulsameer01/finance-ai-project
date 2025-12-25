package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.Category;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // ✅ Used by CategoryService
    List<Category> findByUserId(Long userId);

    // ✅ Used for "user + default" categories
    List<Category> findByUserIdOrUserIdIsNull(Long userId);

    // ✅ Used by CategorySeeder (CRITICAL)
    Optional<Category> findByCategoryNameAndUserIdAndType(
            String categoryName,
            Long userId,
            TransactionType type
    );
}
