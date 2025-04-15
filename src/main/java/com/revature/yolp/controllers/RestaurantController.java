package com.revature.yolp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.yolp.dtos.requests.NewRestaurantRequest;
import com.revature.yolp.dtos.responses.UserSessionDTO;
import com.revature.yolp.models.Restaurant;
import com.revature.yolp.models.Role;
import com.revature.yolp.services.RestaurantService;
import com.revature.yolp.services.SessionService;
import com.revature.yolp.utils.custom_exceptions.InvalidPermissionException;
import com.revature.yolp.utils.custom_exceptions.InvalidSessionException;

import lombok.Data;

@RestController
@RequestMapping("/restaurants")
@Data
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @PostMapping("/add")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody NewRestaurantRequest newRestaurantRequest) {
        UserSessionDTO sessionDTO = sessionService.getCurrentSession();
        if (sessionDTO == null) {
            throw new InvalidSessionException("You must be logged in to add a restaurant");
        }
        if (sessionDTO.getRole() != Role.ADMIN) {
            throw new InvalidPermissionException("You must be an admin to add a restaurant");
        }
        Restaurant newRestaurant = restaurantService.addRestaurant(newRestaurantRequest);
        return ResponseEntity.ok(newRestaurant);
    }
}
