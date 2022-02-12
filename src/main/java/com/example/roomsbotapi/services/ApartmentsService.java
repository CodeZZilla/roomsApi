package com.example.roomsbotapi.services;

import com.example.roomsbotapi.models.Apartments.Apartments;
import com.example.roomsbotapi.repository.ApartmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class ApartmentsService {

    private final ApartmentsRepository repository;

    @Autowired
    public ApartmentsService(ApartmentsRepository repository) {
        this.repository = repository;
    }


    public List<Apartments> findAll() {
        return repository.findAll();
    }

    public List<Apartments> findAllByCreationDate(String date) {
        return  repository.findAllByCreationDate(date);
    }


    public void delete(Apartments apartments) {
        repository.delete(apartments);
    }

    public void deleteByInternalId(Long id) {
        repository.deleteByInternalId(id);
    }


    public void save(Apartments apartments) {
        repository.save(apartments);
    }


    public Apartments findById(String id) {
        Optional<Apartments> apartments = repository.findById(id);
        return apartments.orElse(null);
    }

    public Apartments findByInternalId(Long id) throws ExecutionException, InterruptedException {
        return repository.findByInternalId(id).get();
    }


}
