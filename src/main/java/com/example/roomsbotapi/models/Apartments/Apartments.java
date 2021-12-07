package com.example.roomsbotapi.models.Apartments;

import com.example.roomsbotapi.models.Apartments.pojos.Area;
import com.example.roomsbotapi.models.Apartments.pojos.Location;
import com.example.roomsbotapi.models.Apartments.pojos.Price;
import com.example.roomsbotapi.models.Apartments.pojos.SalesAgent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Apartments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Apartments {

    @Id
    private String Id;

    private Long internalId;

    private String creationDate;
    private String lastUpdateDate;

    private String type;
    private String propertyType;
    private String category;
    private Integer rooms;

    private Area area;

    private Integer floor;
    private Integer floorsTotal;

    private Price price;
    private Location location;

    private String url;

    private SalesAgent salesAgent;

    private String description;

    private List<String> images;

    private String urlTelegraph;
}
