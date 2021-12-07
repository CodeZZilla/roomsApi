package com.example.roomsbotapi.models.Apartments.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private String country;
    private String region;
    private String locationName;
    private String subLocationName;
    private String nonAdminSubLocality;
    private String address;
    private String house;

    private Metro metro;

}
