package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.User;
import com.example.roomsbotapi.services.TelegramApiService;
import com.example.roomsbotapi.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private UserService userService;
    private TelegramApiService telegramApiService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        Collections.reverse(users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getPhoto/{idTelegram}")
    public ResponseEntity<byte[]> getPhotoUser(@PathVariable String idTelegram) throws JsonProcessingException, ExecutionException, InterruptedException {
        byte[] image = telegramApiService.getUserPhoto(idTelegram).get();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/{idTelegram}")
    @ResponseBody
    public ResponseEntity<User> getOneUser(@PathVariable String idTelegram) {
        User user = userService.findByIdTelegram(idTelegram);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/subscriptionLessTwoDays")
    @ResponseBody
    public ResponseEntity<List<String>> updateDaysOfSubscription() {
        List<User> allUsers = userService.findAll();

        List<String> idUsers = allUsers.stream()
                .filter(x -> x.getDaysOfSubscription() <= 2 && x.getDaysOfSubscription() > 0)
                .map(User::getIdTelegram)
                .collect(Collectors.toList());

        return ResponseEntity.ok(idUsers);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        userService.todayCompilationUser(user);
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }


    @PutMapping("/updateById/{id}")
    public ResponseEntity<User> updateById(@PathVariable String id, @RequestBody User user) {
        User userFromDb = userService.findById(id);

        if (userFromDb == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userFromDb.setName(user.getName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setNickname(user.getNickname());
        userFromDb.setSavedApartments(user.getSavedApartments());
        userFromDb.setIdTelegram(user.getIdTelegram());
        userFromDb.setDaysOfSubscription(user.getDaysOfSubscription());
        userFromDb.setRooms(user.getRooms());
        userFromDb.setUserStatus(user.getUserStatus());
        userFromDb.setPriceMin(user.getPriceMin());
        userFromDb.setPriceMax(user.getPriceMax());
        userFromDb.setCity(user.getCity());
        userFromDb.setRegion(user.getRegion());
        userFromDb.setMetroNames(user.getMetroNames());
        userFromDb.setTodayCompilation(user.getTodayCompilation());
        userFromDb.setFreeCounterSearch(user.getFreeCounterSearch());
        userFromDb.setType(user.getType());
        userFromDb.setLanguage(user.getLanguage());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setPhoneNumber(user.getPhoneNumber());
        userFromDb.setNewApartments(user.getNewApartments());

        userService.todayCompilationUser(userFromDb);
        userService.save(userFromDb);

        return ResponseEntity.ok(userFromDb);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") String[] id) {
        try {
            List<User> users = new ArrayList<>();

            for (String item : id) {
                User user = userService.findById(item);
                if (user != null) {
                    users.add(user);
                }
            }

            userService.deleteAll(users);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("id=" + Arrays.toString(id) + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
