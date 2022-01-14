package com.example.roomsbotapi.services;

import com.example.roomsbotapi.models.Apartments.Apartments;
import com.example.roomsbotapi.models.User;
import com.example.roomsbotapi.repository.ApartmentsRepository;
import com.example.roomsbotapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final ApartmentsRepository apartmentsService;
    private RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository repository, ApartmentsRepository apartmentsService) {
        this.repository = repository;
        this.apartmentsService = apartmentsService;
    }


    public List<User> findAll() {
        List<User> user = repository.findAll();
        user.forEach(item -> {
            if (item.getIdTelegram() == null)
                item.setIdTelegram("");

            if (item.getLastName() == null)
                item.setLastName("");

            if (item.getName() == null)
                item.setName("");

        });

        return user;
    }

    public void deleteAll(List<User> users) {
        repository.deleteAll(users);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User findById(String id) {
        return repository.findById(id).get();
    }

    public User findByIdTelegram(String id) {
        return repository.findByIdTelegram(id);
    }

//    @Async
    public void saveAll(List<User> users) {
        repository.saveAll(users);
    }

//    @Async
    public void todayCompilationUser(User user) {
        if (user.getType() != null && user.getCity() != null
                && user.getPriceMin() >= 0 && user.getPriceMax() != 0) {
            String[] type = user.getType().split(":");
            Set<Apartments> apartments = new HashSet<>();

            if (type.length == 2) {
                if (type[1].equals("комната")) {
                    if (user.getRooms() != null)
                        user.setRooms(null);

                    if (user.getRegion() != null && user.getMetroNames() != null)
                        for (String region : user.getRegion())
                            for (String metro : user.getMetroNames()) {
                                apartments.addAll(apartmentsService.findByTypeCityCategoryPriceRegionMetro(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), region, metro));
                                apartments.addAll(apartmentsService.findByTypeCityCategoryPriceRegionMetro(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), region, ""));
                            }
                    else if (user.getRegion() == null && user.getMetroNames() != null)
                        for (String metro : user.getMetroNames())
                            apartments.addAll(apartmentsService.findByTypeCityCategoryPriceMetro(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), metro));
                    else if (user.getRegion() != null && user.getMetroNames() == null)
                        for (String region : user.getRegion())
                            apartments.addAll(apartmentsService.findByTypeCityCategoryPriceRegion(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), region));
                    else
                        apartments.addAll(apartmentsService.findByTypeCityCategoryPrice(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax()));
                } else if (type[1].equals("квартира")) {
                    if (user.getRooms() != null) {
                        if (user.getRegion() != null && user.getMetroNames() != null)
                            for (String region : user.getRegion())
                                for (String metro : user.getMetroNames())
                                    for (int room : user.getRooms()) {
                                        apartments.addAll(apartmentsService.findByTypeCityCategoryPriceRoomsRegionMetro(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), room, region, metro));
                                        apartments.addAll(apartmentsService.findByTypeCityCategoryPriceRoomsRegionMetro(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), room, region, ""));
                                    }

                        else if (user.getRegion() == null && user.getMetroNames() != null)
                            for (String metro : user.getMetroNames())
                                for (int room : user.getRooms())
                                    apartments.addAll(apartmentsService.findByTypeCityCategoryPriceRoomsMetro(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), room, metro));
                        else if (user.getRegion() != null && user.getMetroNames() == null)
                            for (String region : user.getRegion())
                                for (int room : user.getRooms())
                                    apartments.addAll(apartmentsService.findByTypeCityCategoryPriceRoomsRegion(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), room, region));
                        else
                            for (int room : user.getRooms())
                                apartments.addAll(apartmentsService.findByTypeCityCategoryPriceRooms(type[0], user.getCity(), type[1], user.getPriceMin(), user.getPriceMax(), room));
                    }
                }

            } else if (type.length == 1) {

                if (user.getRooms() != null) {
                    if (user.getRegion() != null && user.getMetroNames() != null)
                        for (String region : user.getRegion())
                            for (String metro : user.getMetroNames())
                                for (int room : user.getRooms()) {
                                    apartments.addAll(apartmentsService.findByTypeCityPriceRoomsRegionMetro(type[0], user.getCity(), user.getPriceMin(), user.getPriceMax(), room, region, metro));
                                    apartments.addAll(apartmentsService.findByTypeCityPriceRoomsRegionMetro(type[0], user.getCity(), user.getPriceMin(), user.getPriceMax(), room, region, ""));
                                }
                    else if (user.getRegion() == null && user.getMetroNames() != null)
                        for (String metro : user.getMetroNames())
                            for (int room : user.getRooms())
                                apartments.addAll(apartmentsService.findByTypeCityPriceRoomsMetro(type[0], user.getCity(), user.getPriceMin(), user.getPriceMax(), room, metro));
                    else if (user.getRegion() != null && user.getMetroNames() == null)
                        for (String region : user.getRegion())
                            for (int room : user.getRooms())
                                apartments.addAll(apartmentsService.findByTypeCityPriceRoomsRegion(type[0], user.getCity(), user.getPriceMin(), user.getPriceMax(), room, region));
                    else
                        for (int room : user.getRooms())
                            apartments.addAll(apartmentsService.findByTypeCityPriceRooms(type[0], user.getCity(), user.getPriceMin(), user.getPriceMax(), room));
                }

            }

            List<Long> internalId = apartments.stream().map(Apartments::getInternalId).collect(Collectors.toList());
            Collections.shuffle(internalId);

            user.setTodayCompilation(internalId);
        }
    }


}
