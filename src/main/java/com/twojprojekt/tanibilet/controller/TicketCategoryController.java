package com.twojprojekt.tanibilet.controller;

import com.twojprojekt.tanibilet.model.Event;
import com.twojprojekt.tanibilet.model.TicketCategory;
import com.twojprojekt.tanibilet.repository.EventRepository;
import com.twojprojekt.tanibilet.repository.TicketCategoryRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticketCategories")
public class TicketCategoryController {

    private final TicketCategoryRepository categoryRepo;
    private final EventRepository eventRepo;

    public TicketCategoryController(TicketCategoryRepository categoryRepo,
                                    EventRepository eventRepo) {
        this.categoryRepo = categoryRepo;
        this.eventRepo = eventRepo;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TicketCategory createCategory(@RequestBody TicketCategoryRequest request) {
        Event event = eventRepo.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Wydarzenie nie istnieje"));

        TicketCategory category = new TicketCategory();
        category.setEvent(event);
        category.setName(request.getName());
        category.setPrice(request.getPrice());
        category.setAvailable(request.getAvailable());

        return categoryRepo.save(category);
    }

    public static class TicketCategoryRequest {
        private Long eventId;
        private String name;
        private double price;
        private int available;

        // gettery/settery
        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getAvailable() { return available; }
        public void setAvailable(int available) { this.available = available; }
    }
}
