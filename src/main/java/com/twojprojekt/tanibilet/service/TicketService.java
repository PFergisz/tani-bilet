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
    private final EventRepository eventRepo;

    public TicketService(TicketCategoryRepository categoryRepo, TicketPurchaseRepository purchaseRepo, UserRepository userRepo, EventRepository eventRepo) {
        this.categoryRepo = categoryRepo;
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
    }

    public List<TicketCategory> getCategoriesForEvent(Long eventId) {
        return categoryRepo.findByEventId(eventId);
    }

    @Transactional
    public TicketPurchase buyTicket(String username, Long categoryId, int quantity){
        if(quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");

        TicketCategory category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new RuntimeException("Category not found"));
        if (category.getAvailable() < quantity){
            throw new RuntimeException("Not enough available");
        }
        category.setAvailable(category.getAvailable() - quantity);
        categoryRepo.save(category);

        //Przeliczenie sumy wszystkich biletów w kategoriach
        Event event = category.getEvent();
        int total = categoryRepo.findByEventId(event.getId())
                .stream()
                .mapToInt(TicketCategory::getAvailable)
                .sum();
        event.setAvailable(total);
        eventRepo.save(event);

        User user = userRepo.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User not found"));

        TicketPurchase purchase = new TicketPurchase();
        purchase.setUser(user);
        purchase.setCategory(category);
        purchase.setQuantity(quantity);
        purchase.setPurchaseTime(LocalDateTime.now());

        return purchaseRepo.save(purchase);
    }

    // pomocnicza metoda do przeliczania zewnętrznego użytku
    @Transactional
    public void recalcEventAvailableFromCategories(Long eventId){
        Event event = eventRepo.findById(eventId)
                .orElseThrow(()->new RuntimeException("Event not found"));
        int total = categoryRepo.findByEventId(eventId)
                .stream()
                .mapToInt(TicketCategory::getAvailable)
                .sum();
        event.setAvailable(total);
        eventRepo.save(event);
    }
}
