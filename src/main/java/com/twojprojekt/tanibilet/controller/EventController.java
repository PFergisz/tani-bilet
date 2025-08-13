package com.twojprojekt.tanibilet.controller;

import com.twojprojekt.tanibilet.model.Event;
import com.twojprojekt.tanibilet.repository.EventRepository;
import com.twojprojekt.tanibilet.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
//    private final EventService service;
//    public EventController(EventService service) {
//        this.service = service;
//    }
    private final EventRepository eventRepository;
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Event create(@RequestBody Event e){
//        return service.create(e);
//    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

//    @GetMapping
//    public List<Event> list(){
//        return service.list();
//    }
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event with id " + id + " not found"));
    }
}
