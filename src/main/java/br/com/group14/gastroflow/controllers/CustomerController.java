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

import br.com.group14.gastroflow.dtos.create.CustomerCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.CustomerResponseDTO;
import br.com.group14.gastroflow.dtos.update.CustomerUpdateDTO;
import br.com.group14.gastroflow.dtos.update.PasswordUpdateDTO;
import br.com.group14.gastroflow.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/customers")
@AllArgsConstructor
@Tag(name = "Customer Controller", description = "Endpoints for managing customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @Operation(summary = "List all customers", description = "Returns a paginated list of all registered customers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getFindAllCustomers(Pageable pageable) {
        logger.info("GET  => /customers - Request: {}", pageable);
        Page<CustomerResponseDTO> customers = customerService.findAll(pageable);
        // logger.info("Response: {}", customers);
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Get customer by ID", description = "Returns a single customer by their unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = CustomerResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(
            @PathVariable("id") Long id) {
        logger.info("GET  => /customers/{}", id);
        var customerDTO = customerService.findById(id);
        return ResponseEntity.ok(customerDTO);
    }

    @Operation(summary = "Search customers by name", description = "Returns a paginated list of customers whose name contains the given string")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("search/{name}")
    public ResponseEntity<Page<CustomerResponseDTO>> getFindByName(
            @PathVariable String name,
            Pageable pageable) {
        logger.info("GET  => /customers/search/{name} - Request: {}", pageable);
        Page<CustomerResponseDTO> customers = customerService.findByName(name, pageable);

        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Create customer", description = "Creates a new customer and returns the created resource")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(implementation = CustomerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "CPF, email or login already registered", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> post(
            @Valid @RequestBody CustomerCreateDTO customerCreateDTO) {
        logger.info("POST => /customers - Body: {}", customerCreateDTO);

        CustomerResponseDTO customerResponseDTO = customerService.save(customerCreateDTO);

        URI uri = URI.create("/customers/" + customerResponseDTO.getId());
        return ResponseEntity.created(uri).body(customerResponseDTO);
    }

    @Operation(summary = "Update customer", description = "Updates an existing customer by ID and returns the updated resource")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully", content = @Content(schema = @Schema(implementation = CustomerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> put(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        logger.info("PUT => /customers/{} - Body: {}", id, customerUpdateDTO);

        CustomerResponseDTO customerResponseDTO = customerService.update(customerUpdateDTO, id);
        return ResponseEntity.ok(customerResponseDTO);
    }

    @Operation(summary = "Update customer password", description = "Updates the password for an existing customer by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody PasswordUpdateDTO updatePasswordDTO) {
        customerService.updatePassword(updatePasswordDTO, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete customer", description = "Deletes an existing customer by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
