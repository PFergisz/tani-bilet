package com.twojprojekt.tanibilet.service;

import com.twojprojekt.tanibilet.model.*;
import com.twojprojekt.tanibilet.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {
    private final TicketCategoryRepository categoryRepo;
    private final TicketPurchaseRepository purchaseRepo;
    private final UserRepository userRepo;

    public TicketService(TicketCategoryRepository categoryRepo, TicketPurchaseRepository purchaseRepo, UserRepository userRepo) {
        this.categoryRepo = categoryRepo;
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
    }

    public List<TicketCategory> getCategoriesForEvent(Long eventId) {
        return categoryRepo.findByEventId(eventId);
    }

    @Transactional
    public TicketPurchase buyTicket(String username, Long categoryId, int quantity){
        TicketCategory category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new RuntimeException("Category not found"));
        if (category.getAvailable() < quantity){
            throw new RuntimeException("Not enough available");
        }
        category.setAvailable(category.getAvailable() - quantity);
        categoryRepo.save(category);

        User user = userRepo.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User not found"));

        TicketPurchase purchase = new TicketPurchase();
        purchase.setUser(user);
        purchase.setCategory(category);
        purchase.setQuantity(quantity);
        purchase.setPurchaseTime(LocalDateTime.now());

        return purchaseRepo.save(purchase);
    }

}
