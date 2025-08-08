package com.twojprojekt.tanibilet.repository;

import com.twojprojekt.tanibilet.model.TicketPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketPurchaseRepository extends JpaRepository<TicketPurchase, Long>{
}
