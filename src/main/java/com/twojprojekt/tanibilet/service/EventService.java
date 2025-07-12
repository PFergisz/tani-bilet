package com.twojprojekt.tanibilet.service;

import com.twojprojekt.tanibilet.model.Event;
import com.twojprojekt.tanibilet.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository repo;
    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public Event create(Event e){
        return repo.save(e);
    }

    public List<Event> list(){
        return repo.findAll();
    }
}
