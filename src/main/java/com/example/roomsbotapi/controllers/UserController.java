package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.User;
import com.example.roomsbotapi.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private UserService userService;
    private RestTemplate restTemplate;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        Collections.reverse(users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getPhoto/{idTelegram}")
    public ResponseEntity<byte[]> getPhotoUser(@PathVariable String idTelegram) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        headers.setContentType(MediaType.IMAGE_JPEG);

        ResponseEntity<byte[]> notFoundImage = restTemplate.getForEntity(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlf91yfOT2B7vCu4ikHj54dlXtsCAo7ZzeCw&usqp=CAU",
                byte[].class);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "https://api.telegram.org/bot2069670508:AAFR_4gwUKymhGc7oiTLvq17d-nyYm6mY6A/getUserProfilePhotos?user_id=" + idTelegram,
                    String.class);
            root = mapper.readTree(response.getBody());
        } catch (Exception e) {
            headers.setContentLength(Objects.requireNonNull(notFoundImage.getBody()).length);
            return new ResponseEntity<>(notFoundImage.getBody(), headers, HttpStatus.OK);
        }

        if (root.path("result").path("photos").toString().equals("[]")) {
            headers.setContentLength(Objects.requireNonNull(notFoundImage.getBody()).length);
            return new ResponseEntity<>(notFoundImage.getBody(), headers, HttpStatus.OK);
        }

        String fileId = root.path("result").path("photos").get(0).get(1).path("file_id").toString().replace('"', ' ').trim();
        ResponseEntity<String> secondResponse = restTemplate.getForEntity(
                "https://api.telegram.org/bot2069670508:AAFR_4gwUKymhGc7oiTLvq17d-nyYm6mY6A/getFile?file_id=" + fileId,
                String.class
        );

        root = mapper.readTree(secondResponse.getBody());
        String filePath = root.path("result").path("file_path").toString().replace('"', ' ').trim();

        ResponseEntity<byte[]> imageString = restTemplate.getForEntity("https://api.telegram.org/file/bot2069670508:AAFR_4gwUKymhGc7oiTLvq17d-nyYm6mY6A/" + filePath,
                byte[].class);
        byte[] image = imageString.getBody();

        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(Objects.requireNonNull(image).length);

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
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
