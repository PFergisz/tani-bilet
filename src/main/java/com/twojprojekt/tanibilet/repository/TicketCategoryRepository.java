package com.twojprojekt.tanibilet.repository;

import com.twojprojekt.tanibilet.model.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    List<TicketCategory> findByEventId(Long eventId);
}
