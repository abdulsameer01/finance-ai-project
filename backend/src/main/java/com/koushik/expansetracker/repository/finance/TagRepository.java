package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(String tagName);
}
