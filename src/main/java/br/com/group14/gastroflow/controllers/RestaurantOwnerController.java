package br.com.group14.gastroflow.controllers;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/restaurant-owners")
@AllArgsConstructor
@Tag(name = "Restaurant Owner Controller", description = "Endpoints for managing restaurant owners")
public class RestaurantOwnerController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantOwnerController.class);

    private final RestaurantOwnerService restaurantOwnerService;

    @Operation(summary = "List all restaurant owners", description = "Returns a paginated list of all registered restaurant owners")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant owners retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<RestaurantOwnerResponseDTO>> getFindAllRestaurantOwners(Pageable pageable) {
        logger.info("GET  => /restaurant-owners - Request: {}", pageable);
        Page<RestaurantOwnerResponseDTO> restaurantOwners = restaurantOwnerService.findAll(pageable);
        // logger.info("Response: {}", restaurantOwners);
        return ResponseEntity.ok(restaurantOwners);
    }

    @Operation(summary = "Get restaurant owner by ID", description = "Returns a single restaurant owner by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant owner found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = RestaurantOwnerResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant owner not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantOwnerResponseDTO> getById(
            @PathVariable("id") Long id) {
        logger.info("GET  => /restaurant-owners/{}", id);
        var restaurantOwnerDTO = restaurantOwnerService.findById(id);
        return ResponseEntity.ok(restaurantOwnerDTO);
    }

    @Operation(summary = "Search restaurant owners by name", description = "Returns a paginated list of restaurant owners whose name contains the given string")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("search/{name}")
    public ResponseEntity<Page<RestaurantOwnerResponseDTO>> getFindByName(
            @PathVariable String name,
            Pageable pageable) {
        logger.info("GET  => /restaurant-owners/search/{name} - Request: {}", pageable);
        Page<RestaurantOwnerResponseDTO> restaurantOwners = restaurantOwnerService.findByName(name, pageable);

        return ResponseEntity.ok(restaurantOwners);
    }

    @Operation(summary = "Create a new restaurant owner", description = "Creates a new restaurant owner and returns it in the response body")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurant owner created", content = @Content(schema = @Schema(implementation = RestaurantOwnerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Email or login already registered", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<RestaurantOwnerResponseDTO> post(
            @Valid @RequestBody RestaurantOwnerCreateDTO restaurantOwnerCreateDTO) {
        logger.info("POST => /restaurant-owners - Body: {}", restaurantOwnerCreateDTO);

        RestaurantOwnerResponseDTO restaurantOwnerResponseDTO = restaurantOwnerService.save(restaurantOwnerCreateDTO);

        URI uri = URI.create("/restaurant-owners/" + restaurantOwnerResponseDTO.getId());
        return ResponseEntity.created(uri).body(restaurantOwnerResponseDTO);
    }

    @Operation(summary = "Update restaurant owner", description = "Updates an existing restaurant owner by ID and returns the updated resource")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurant owner updated successfully", content = @Content(schema = @Schema(implementation = RestaurantOwnerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant owner not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantOwnerResponseDTO> put(
            @PathVariable("id") Long id,
            @Valid @RequestBody RestaurantOwnerUpdateDTO restaurantOwnerUpdateDTO) {
        logger.info("PUT => /restaurant-owners/{} - Body: {}", id, restaurantOwnerUpdateDTO);

        RestaurantOwnerResponseDTO restaurantOwnerResponseDTO = restaurantOwnerService.update(restaurantOwnerUpdateDTO,
                id);
        return ResponseEntity.ok(restaurantOwnerResponseDTO);
    }

    @Operation(summary = "Update restaurant owner password", description = "Updates the password for an existing restaurant owner by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant owner not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody PasswordUpdateDTO updatePasswordDTO) {
        restaurantOwnerService.updatePassword(updatePasswordDTO, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete restaurant owner", description = "Deletes an existing restaurant owner by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurant owner deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant owner not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        restaurantOwnerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
