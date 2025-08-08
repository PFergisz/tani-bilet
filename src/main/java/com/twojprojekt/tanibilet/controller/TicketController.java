package com.twojprojekt.tanibilet.controller;

import com.twojprojekt.tanibilet.model.TicketCategory;
import com.twojprojekt.tanibilet.model.TicketPurchase;
import com.twojprojekt.tanibilet.service.TicketService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService service;

    public TicketController(TicketService service){
        this.service = service;
    }

    @GetMapping("/event/{eventId}")
    public List<TicketCategory> getCategories(@PathVariable Long eventId){
        return service.getCategoriesForEvent(eventId);
    }

    @PostMapping("/buy")
    public TicketPurchase buyTicket(@AuthenticationPrincipal UserDetails user, @RequestParam Long categoryId, @RequestParam int quantity){
        return service.buyTicket(user.getUsername(), categoryId, quantity);
    }
}
