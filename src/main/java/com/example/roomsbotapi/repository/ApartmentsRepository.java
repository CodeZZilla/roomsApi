package com.example.roomsbotapi.repository;

import com.example.roomsbotapi.models.Apartments.Apartments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentsRepository extends MongoRepository<Apartments, String> {

    Apartments findByInternalId(Long id);

    //1
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2}")
    List<Apartments> findByTypeCityCategory(String type, String city, String category);

    //2
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2, 'price.value': {$gte: ?3, $lte: ?4}}")
    List<Apartments> findByTypeCityCategoryPrice(String type, String city, String category, int priceMin, int priceMax);

    //3
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2, 'price.value': {$gte: ?3, $lte: ?4}, " +
            "'location.subLocationName': ?5, 'location.metro.name': ?6}")
    List<Apartments> findByTypeCityCategoryPriceRegionMetro(String type, String city, String category, int priceMin, int priceMax, String subLocationName, String metro);

    //4
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2, 'price.value': {$gte: ?3, $lte: ?4}, 'location.metro.name': ?5}")
    List<Apartments> findByTypeCityCategoryPriceMetro(String type, String city, String category, int priceMin, int priceMax, String metro);

    //5
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2, 'price.value': {$gte: ?3, $lte: ?4}, 'location.subLocationName': ?5}")
    List<Apartments> findByTypeCityCategoryPriceRegion(String type, String city, String category, int priceMin, int priceMax, String subLocationName);


    //------------------------------------------------
//2
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2, 'price.value': {$gte: ?3, $lte: ?4}, 'rooms': ?5}")
    List<Apartments> findByTypeCityCategoryPriceRooms(String type, String city, String category, int priceMin, int priceMax, int rooms);

    //3
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2, 'price.value': {$gte: ?3, $lte: ?4}, 'rooms': ?5, " +
            "'location.subLocationName': ?6, 'location.metro.name': ?7}")
    List<Apartments> findByTypeCityCategoryPriceRoomsRegionMetro(String type, String city, String category, int priceMin, int priceMax, int rooms, String subLocationName, String metro);

    //4
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2, 'price.value': {$gte: ?3, $lte: ?4}, 'rooms': ?5, 'location.metro.name': ?6}")
    List<Apartments> findByTypeCityCategoryPriceRoomsMetro(String type, String city, String category, int priceMin, int priceMax, int rooms, String metro);

    //5
    @Query("{'type': ?0, 'location.locationName': ?1, 'category': ?2, 'price.value': {$gte: ?3, $lte: ?4}, 'rooms': ?5 'location.subLocationName': ?6}")
    List<Apartments> findByTypeCityCategoryPriceRoomsRegion(String type, String city, String category, int priceMin, int priceMax, int rooms, String subLocationName);
    //------------------------------------------------


    //1
    @Query("{'type': ?0, 'location.locationName': ?1}")
    List<Apartments> findByTypeCity(String type, String city);

    //2
    @Query("{'type': ?0, 'location.locationName': ?1, 'price.value': {$gte: ?2, $lte: ?3}}")
    List<Apartments> findByTypeCityPrice(String type, String city, int priceMin, int priceMax);

    //3
    @Query("{'type': ?0, 'location.locationName': ?1, 'price.value': {$gte: ?2, $lte: ?3}, 'rooms': ?4}")
    List<Apartments> findByTypeCityPriceRooms(String type, String city, int priceMin, int priceMax, int rooms);

    //4
    @Query("{'type': ?0, 'location.locationName': ?1, 'price.value': {$gte: ?2, $lte: ?3}, 'rooms': ?4, " +
            "'location.subLocationName': ?5, 'location.metro.name': ?6}")
    List<Apartments> findByTypeCityPriceRoomsRegionMetro(String type, String city, int priceMin, int priceMax, int rooms, String subLocationName, String metro);

    //5
    @Query("{'type': ?0, 'location.locationName': ?1, 'price.value': {$gte: ?2, $lte: ?3}, 'rooms': ?4, 'location.metro.name': ?5}")
    List<Apartments> findByTypeCityPriceRoomsMetro(String type, String city, int priceMin, int priceMax, int rooms, String metro);

    //6
    @Query("{'type': ?0, 'location.locationName': ?1, 'price.value': {$gte: ?2, $lte: ?3}, 'rooms': ?4, 'location.subLocationName': ?5}")
    List<Apartments> findByTypeCityPriceRoomsRegion(String type, String city, int priceMin, int priceMax, int rooms, String subLocationName);

    //7
    @Query("{'type': ?0, 'location.locationName': ?1,  'price.value': {$gte: ?2, $lte: ?3}, " +
            "'location.subLocationName': ?4, 'location.metro.name': ?5}")
    List<Apartments> findByTypeCityPriceRegionMetro(String type, String city, int priceMin, int priceMax, String subLocationName, String metro);

    //8
    @Query("{'type': ?0, 'location.locationName': ?1, 'price.value': {$gte: ?2, $lte: ?3}, " +
            "'location.subLocationName': ?4}")
    List<Apartments> findByTypeCityPriceRegion(String type, String city, int priceMin, int priceMax, String subLocationName);

    //9
    @Query("{'type': ?0, 'location.locationName': ?1,  'price.value': {$gte: ?2, $lte: ?3}, 'location.metro.name': ?4}")
    List<Apartments> findByTypeCityPriceMetro(String type, String city, int priceMin, int priceMax, String metro);

}
