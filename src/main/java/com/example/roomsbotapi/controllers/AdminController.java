package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.User;
import com.example.roomsbotapi.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin
@Slf4j
public class AdminController {

    private final UserService userService;


    @GetMapping("/dateStatistic")
    public Map<String, Integer> getDateStatistic() {
        Map<String, Integer> response = new HashMap<>();
        List<User> users = userService.findAll();

        int dayNow = LocalDate.now().getDayOfMonth();
        int monthNow = LocalDate.now().getMonthOfYear();
        int yearNow = LocalDate.now().getYear();

        System.out.println(dayNow + " " + monthNow + " " + yearNow);

        int days = (int) users.stream().filter(item -> dayNow == item.getCreationDate().toLocalDate().getDayOfMonth() &&
                monthNow == item.getCreationDate().toLocalDate().getMonthOfYear() &&
                yearNow == item.getCreationDate().toLocalDate().getYear()).count();

        int week = (int) users.stream().filter(item -> yearNow == item.getCreationDate().toLocalDate().getYear() &&
                monthNow == item.getCreationDate().toLocalDate().getMonthOfYear() &&
                LocalDate.now().getWeekOfWeekyear() == item.getCreationDate().toLocalDate().getWeekOfWeekyear()).count();

        int month = (int) users.stream().filter(item -> monthNow == item.getCreationDate().toLocalDate().getMonthOfYear() &&
                yearNow == item.getCreationDate().toLocalDate().getYear()).count();

        response.put("allUsers", users.size());
        response.put("forDay", days);
        response.put("forWeek", week);
        response.put("forMonth", month);

        return response;
    }

    @GetMapping("/stagesAtWhichClientsStopped")
    public Map<String, Integer> stagesAtWhichClientsStopped() {
        List<User> users = userService.findAll();
        Map<String, Integer> response = new HashMap<>();

        response.put("zeroStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 0).count());
        response.put("firstStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 1).count());
        response.put("secondStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 2).count());
        response.put("thirdStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 3).count());
        response.put("fourthStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 4).count());
        response.put("fifthStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 5).count());
        response.put("sixthStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 6).count());
        response.put("seventhStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 7).count());

        return response;
    }

    @GetMapping("/dataForChats")
    public Map<String, List<Object>> dataForChats() {
        List<User> users = userService.findAll();
        Map<String, List<Object>> response = new HashMap<>();
        List<Integer> usersCount = new ArrayList<>();

        Set<LocalDate> dates = users.stream().map(x -> x.getCreationDate().toLocalDate())
                .collect(Collectors.toSet());

        List<LocalDate> dateList = dates.stream()
                .filter(item -> Days.daysBetween(item, LocalDate.now()).getDays() <= 10)
                .sorted()
                .collect(Collectors.toList());

        for (var date : dateList) {
            int countUsers = (int) users.stream().filter(x -> x.getCreationDate().toLocalDate().equals(date)).count();
            usersCount.add(countUsers);
        }

        response.put("dates", Arrays.asList(dateList.toArray()));
        response.put("users", Arrays.asList(usersCount.toArray()));

        log.info(response.toString());
        return response;
    }
}
