package br.com.group14.gastroflow.controllers;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.group14.gastroflow.dtos.create.RestaurantOwnerCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.RestaurantOwnerResponseDTO;
import br.com.group14.gastroflow.dtos.update.PasswordUpdateDTO;
import br.com.group14.gastroflow.dtos.update.RestaurantOwnerUpdateDTO;
import br.com.group14.gastroflow.services.RestaurantOwnerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/restaurant-owners")
@AllArgsConstructor
public class RestaurantOwnerController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantOwnerController.class);

    private final RestaurantOwnerService restaurantOwnerService;

    @GetMapping
    public ResponseEntity<Page<RestaurantOwnerResponseDTO>> getFindAllRestaurantOwners(Pageable pageable) {
        logger.info("GET  => /restaurant-owners - Request: {}", pageable);
        Page<RestaurantOwnerResponseDTO> restaurantOwners = restaurantOwnerService.findAll(pageable);
        // logger.info("Response: {}", restaurantOwners);
        return ResponseEntity.ok(restaurantOwners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantOwnerResponseDTO> getById(
            @PathVariable("id") Long id) {
        logger.info("GET  => /restaurant-owners/{}", id);
        var restaurantOwnerDTO = restaurantOwnerService.findById(id);
        return ResponseEntity.ok(restaurantOwnerDTO);
    }

    @GetMapping("search/{name}")
    public ResponseEntity<Page<RestaurantOwnerResponseDTO>> getFindByName(
            @PathVariable String name,
            Pageable pageable) {
        logger.info("GET  => /restaurant-owners/search/{name} - Request: {}", pageable);
        Page<RestaurantOwnerResponseDTO> restaurantOwners = restaurantOwnerService.findByName(name, pageable);

        return ResponseEntity.ok(restaurantOwners);
    }

    @PostMapping
    public ResponseEntity<RestaurantOwnerResponseDTO> post(
            @Valid @RequestBody RestaurantOwnerCreateDTO restaurantOwnerCreateDTO) {
        logger.info("POST => /restaurant-owners - Body: {}", restaurantOwnerCreateDTO);

        RestaurantOwnerResponseDTO restaurantOwnerResponseDTO = restaurantOwnerService.save(restaurantOwnerCreateDTO);

        URI uri = URI.create("/restaurant-owners/" + restaurantOwnerResponseDTO.getId());
        return ResponseEntity.created(uri).body(restaurantOwnerResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantOwnerResponseDTO> put(
            @PathVariable("id") Long id,
            @Valid @RequestBody RestaurantOwnerUpdateDTO restaurantOwnerUpdateDTO) {
        logger.info("PUT => /restaurant-owners/{} - Body: {}", id, restaurantOwnerUpdateDTO);

        RestaurantOwnerResponseDTO restaurantOwnerResponseDTO = restaurantOwnerService.update(restaurantOwnerUpdateDTO,
                id);
        return ResponseEntity.ok(restaurantOwnerResponseDTO);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody PasswordUpdateDTO updatePasswordDTO) {
        restaurantOwnerService.updatePassword(updatePasswordDTO, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        restaurantOwnerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
