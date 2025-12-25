package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.BillReminderRequest;
import com.koushik.expansetracker.dto.BillReminderResponse;
import com.koushik.expansetracker.entity.finance.BillReminder;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.service.finance.interfaces.BillReminderServiceInterface;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class BillReminderController {

    private final BillReminderServiceInterface service;
    private final FinanceMapper mapper;

    @PostMapping
    public ResponseEntity<BillReminderResponse> createReminder(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody BillReminderRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();

        BillReminder saved = service.createReminder(
                mapper.toBillReminderEntity(request, userId)
        );

        return ResponseEntity.ok(mapper.toBillReminderResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<BillReminderResponse>> getReminders(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();

        var list = service.getRemindersForUser(userId)
                .stream()
                .map(mapper::toBillReminderResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<BillReminderResponse>> getUpcoming(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        Long userId = currentUser.getUser().getUserId();

        var list = service.getUpcomingReminders(userId, from, to)
                .stream()
                .map(mapper::toBillReminderResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{reminderId}")
    public ResponseEntity<BillReminderResponse> getReminderById(@PathVariable Long reminderId) {
        BillReminder reminder = service.getReminderById(reminderId);
        return ResponseEntity.ok(mapper.toBillReminderResponse(reminder));
    }

    @PutMapping("/{reminderId}")
    public ResponseEntity<BillReminderResponse> updateReminder(
            @PathVariable Long reminderId,
            @Valid @RequestBody BillReminderRequest request
    ) {
        BillReminder updated = service.updateReminder(reminderId, mapper.toBillReminderEntity(request, null));
        return ResponseEntity.ok(mapper.toBillReminderResponse(updated));
    }

    @DeleteMapping("/{reminderId}")
    public ResponseEntity<String> deleteReminder(@PathVariable Long reminderId) {
        service.deleteReminder(reminderId);
        return ResponseEntity.ok("Bill reminder deleted successfully.");
    }
}
