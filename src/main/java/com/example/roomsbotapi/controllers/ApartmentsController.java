package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.Apartments.Apartments;
import com.example.roomsbotapi.services.ApartmentsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class ApartmentsController {

    private final ApartmentsService apartmentsService;

    @ResponseBody
    @GetMapping("/find")
    public ResponseEntity<List<Apartments>> findByIdRoom(@RequestParam(value = "id") long[] id) {
        if (id == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);

        List<Apartments> apartments = new ArrayList<>();
        for (long item : id) {
            apartments.add(apartmentsService.findByInternalId(item));
        }

        return ResponseEntity.ok(apartments);
    }


    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<List<Apartments>> all() {
        return ResponseEntity.ok(apartmentsService.findAll());
    }

}
