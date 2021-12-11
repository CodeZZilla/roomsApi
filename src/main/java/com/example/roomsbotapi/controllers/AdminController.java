package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.User;
import com.example.roomsbotapi.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        Map<String, Integer> integerMap = new HashMap<>();
        List<User> users = userService.findAll();

        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTime(new Date());
        int dayNow = calendarNow.get(Calendar.DAY_OF_MONTH);
        int monthNow = calendarNow.get(Calendar.MONTH) + 1;
        int yearNow = calendarNow.get(Calendar.YEAR);

        Calendar calendarUser = Calendar.getInstance();

        int days = (int) users.stream().filter(item -> {
            calendarUser.setTime(item.getCreationDate());
            return dayNow == calendarUser.get(Calendar.DAY_OF_MONTH) &&
                    monthNow == calendarUser.get(Calendar.MONTH) + 1 && yearNow == calendarUser.get(Calendar.YEAR);
        }).count();

        int week = (int) users.stream().filter(item -> {
            calendarUser.setTime(item.getCreationDate());
            return yearNow == calendarUser.get(Calendar.YEAR) && monthNow == calendarUser.get(Calendar.MONTH) + 1
                    && calendarNow.get(Calendar.WEEK_OF_MONTH) == calendarUser.get(Calendar.WEEK_OF_MONTH);
        }).count();

        int year = (int) users.stream().filter(item -> {
            calendarUser.setTime(item.getCreationDate());
            return monthNow == calendarUser.get(Calendar.MONTH) + 1 && yearNow == calendarUser.get(Calendar.YEAR);
        }).count();

        integerMap.put("allUsers", users.size());
        integerMap.put("forDay", days);
        integerMap.put("forWeek", week);
        integerMap.put("forMonth", year);

        return integerMap;
    }

    @GetMapping("/stagesAtWhichClientsStopped")
    public Map<String, Integer> stagesAtWhichClientsStopped() {
        List<User> users = userService.findAll();
        Map<String, Integer> mapStages = new HashMap<>();

        mapStages.put("zeroStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 0).count());
        mapStages.put("firstStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 1).count());
        mapStages.put("secondStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 2).count());
        mapStages.put("thirdStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 3).count());
        mapStages.put("fourthStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 4).count());
        mapStages.put("fifthStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 5).count());
        mapStages.put("sixthStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 6).count());
        mapStages.put("seventhStage", (int) users.stream().map(User::getUserStatus).filter(user -> user == 7).count());

        return mapStages;
    }

    @GetMapping("/dataForChats")
    public Map<String, List<Object>> dataForChats() {
        List<User> users = userService.findAll();
        Map<String, List<Object>> datMap = new HashMap<>();
        List<Integer> usersCount = new ArrayList<>();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Set<LocalDate> dates = users.stream().map(x -> LocalDate.parse(format.format(x.getCreationDate())))
                .collect(Collectors.toSet());

        List<LocalDate> dateList = dates.stream().filter(item -> Days.daysBetween(item, LocalDate.now()).getDays() <= 10).sorted().collect(Collectors.toList());

        for (var date : dateList) {

            int countUsers = (int) users.stream().filter(x -> LocalDate.parse(format.format(x.getCreationDate())).equals(date)).count();
            usersCount.add(countUsers);

        }


        datMap.put("dates", Arrays.asList(dateList.toArray()));
        datMap.put("users", Arrays.asList(usersCount.toArray()));

        log.info(datMap.toString());
        return datMap;
    }
}
