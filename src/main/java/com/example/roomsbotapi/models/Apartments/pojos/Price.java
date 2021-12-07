package com.example.roomsbotapi.models.Apartments.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Price {
    private Long value;
    private String currency;

}
