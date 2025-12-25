package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.Category;
import com.koushik.expansetracker.repository.finance.CategoryRepository;
import com.koushik.expansetracker.repository.finance.TransactionRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.CategoryServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements CategoryServiceInterface {

    private final CategoryRepository categoryRepository;
    private final OwnershipValidator ownershipValidator;
    private final TransactionRepository transactionRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           OwnershipValidator ownershipValidator, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.ownershipValidator = ownershipValidator;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getUserCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    @Override
    public List<Category> getAllCategoriesIncludingDefault(Long userId) {
        // Assuming default categories have userId = null
        return categoryRepository.findByUserIdOrUserIdIsNull(userId);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateCategory(categoryId, currentUserId);

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateCategory(categoryId, currentUserId);
        categoryRepository.deleteById(categoryId);
    }
    @Override
    public Category getCategoryByIdAndUser(Long id, Long userId) {
        return categoryRepository.findById(id)
                .filter(cat -> cat.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public Category updateCategory(Long id, Category newData, Long userId) {
        Category existing = getCategoryByIdAndUser(id, userId);

        existing.setCategoryName(newData.getCategoryName());
        existing.setType(newData.getType());

        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategorySafe(Long id, Long userId) {
        Category category = getCategoryByIdAndUser(id, userId);

        if (transactionRepository.existsByCategoryId(id)) {
            throw new RuntimeException("Cannot delete category. It is used in transactions.");
        }

        categoryRepository.deleteById(id);
    }

}
