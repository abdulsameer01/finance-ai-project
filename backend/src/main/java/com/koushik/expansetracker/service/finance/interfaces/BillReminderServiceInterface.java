package com.koushik.expansetracker.service.finance.interfaces;

import com.koushik.expansetracker.entity.finance.BillReminder;

import java.time.LocalDate;
import java.util.List;

public interface BillReminderServiceInterface {

    BillReminder createReminder(BillReminder reminder);

    List<BillReminder> getRemindersForUser(Long userId);

    List<BillReminder> getUpcomingReminders(Long userId, LocalDate from, LocalDate to);

    BillReminder getReminderById(Long reminderId);

    BillReminder updateReminder(Long reminderId, BillReminder updated);

    void deleteReminder(Long reminderId);
}
