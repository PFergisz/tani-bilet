package com.twojprojekt.tanibilet.controller;

import com.twojprojekt.tanibilet.model.Event;
import com.twojprojekt.tanibilet.model.TicketCategory;
import com.twojprojekt.tanibilet.repository.EventRepository;
import com.twojprojekt.tanibilet.repository.TicketCategoryRepository;
import com.twojprojekt.tanibilet.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/ticketCategories")
public class TicketCategoryController {

    private final TicketCategoryRepository categoryRepo;
    private final EventRepository eventRepo;
    private final TicketService ticketService;

    public TicketCategoryController(TicketCategoryRepository categoryRepo,
                                    EventRepository eventRepo, TicketService ticketService) {
        this.categoryRepo = categoryRepo;
        this.eventRepo = eventRepo;
        this.ticketService = ticketService;
    }

    // CREATE
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

        TicketCategory saved =  categoryRepo.save(category);

        ticketService.recalcEventAvailableFromCategories(event.getId());

        return saved;
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TicketCategory updateCategory(@PathVariable Long id, @RequestBody TicketCategoryRequest request) {
        TicketCategory category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategoria nie istnieje"));

        category.setName(request.getName());
        category.setPrice(request.getPrice());
        category.setAvailable(request.getAvailable());

        TicketCategory saved = categoryRepo.save(category);

        Long eventId = saved.getEvent().getId();
        ticketService.recalcEventAvailableFromCategories(eventId);

        return saved;
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id){
        TicketCategory category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategoria nie istnieje"));
        Long eventId = category.getEvent().getId();

        categoryRepo.deleteById(id);

        ticketService.recalcEventAvailableFromCategories(eventId);
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
