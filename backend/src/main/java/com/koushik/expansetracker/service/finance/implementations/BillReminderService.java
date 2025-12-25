package com.koushik.expansetracker.service.finance.implementations;
import com.koushik.expansetracker.entity.finance.BillReminder;
import com.koushik.expansetracker.repository.finance.BillReminderRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.BillReminderServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillReminderService implements BillReminderServiceInterface {

    private final BillReminderRepository billReminderRepository;
    private final OwnershipValidator ownershipValidator;

    public BillReminderService(BillReminderRepository billReminderRepository,
                               OwnershipValidator ownershipValidator) {
        this.billReminderRepository = billReminderRepository;
        this.ownershipValidator = ownershipValidator;
    }

    @Override
    public BillReminder createReminder(BillReminder reminder) {
        return billReminderRepository.save(reminder);
    }

    @Override
    public List<BillReminder> getRemindersForUser(Long userId) {
        return billReminderRepository.findByUserId(userId);
    }

    @Override
    public List<BillReminder> getUpcomingReminders(Long userId, LocalDate from, LocalDate to) {
        List<BillReminder> allUserReminders = billReminderRepository.findByUserId(userId);
        return allUserReminders.stream()
                .filter(r -> r.getDueDate() != null)
                .filter(r -> !r.getDueDate().isBefore(from) && !r.getDueDate().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public BillReminder getReminderById(Long reminderId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateBillReminder(reminderId, currentUserId);

        return billReminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Bill reminder not found"));
    }

    @Override
    public BillReminder updateReminder(Long reminderId, BillReminder updated) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateBillReminder(reminderId, currentUserId);

        BillReminder existing = billReminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Bill reminder not found"));

        existing.setBillName(updated.getBillName());
        existing.setAmountDue(updated.getAmountDue());
        existing.setDueDate(updated.getDueDate());
        existing.setStatus(updated.getStatus());

        return billReminderRepository.save(existing);
    }

    @Override
    public void deleteReminder(Long reminderId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateBillReminder(reminderId, currentUserId);
        billReminderRepository.deleteById(reminderId);
    }
}
