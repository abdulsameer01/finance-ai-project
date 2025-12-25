package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.TransactionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionTagRepository extends JpaRepository<TransactionTag, Long> {

    List<TransactionTag> findByTransactionId(Long transactionId);

    List<TransactionTag> findByTagId(Long tagId);

    void deleteByTransactionId(Long transactionId);
}
