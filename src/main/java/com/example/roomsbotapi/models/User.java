package com.example.roomsbotapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Document(collection = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String email;
    private String phoneNumber;

    private Date creationDate = Calendar.getInstance().getTime();

    private String type;
    private String name;
    private String lastName;
    private String nickname;
    private List<Long> savedApartments;
    private String city;
    private List<String> region;
    private List<String> metroNames;
    private String idTelegram;
    private long daysOfSubscription;
    private int priceMin;
    private int priceMax;
    private List<Integer> rooms;
    private List<Long> todayCompilation;
    private String language;
    private int freeCounterSearch;
    private int userStatus;
    private Date usingTime;

}
