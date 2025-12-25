package com.koushik.expansetracker.config;

import com.koushik.expansetracker.entity.finance.Category;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import com.koushik.expansetracker.repository.finance.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategorySeeder {

    private final CategoryRepository categoryRepository;

    public void seedDefaultCategories(Long userId) {

        seed("Food", TransactionType.EXPENSE, userId);
        seed("Transport", TransactionType.EXPENSE, userId);
        seed("Bills", TransactionType.EXPENSE, userId);
        seed("Groceries", TransactionType.EXPENSE, userId);
        seed("Shopping", TransactionType.EXPENSE, userId);
        seed("Salary", TransactionType.INCOME, userId);
    }

    private void seed(String name, TransactionType type, Long userId) {
        categoryRepository
                .findByCategoryNameAndUserIdAndType(name, userId, type)
                .orElseGet(() ->
                        categoryRepository.save(
                                Category.builder()
                                        .categoryName(name)
                                        .type(type)
                                        .userId(userId)
                                        .build()
                        )
                );
    }
}
