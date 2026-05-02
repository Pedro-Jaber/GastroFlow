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

import br.com.group14.gastroflow.dtos.create.CustomerCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.CustomerResponseDTO;
import br.com.group14.gastroflow.dtos.update.CustomerUpdateDTO;
import br.com.group14.gastroflow.dtos.update.PasswordUpdateDTO;
import br.com.group14.gastroflow.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getFindAllCustomers(Pageable pageable) {
        logger.info("GET  => /customers - Request: {}", pageable);
        Page<CustomerResponseDTO> customers = customerService.findAll(pageable);
        // logger.info("Response: {}", customers);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(
            @PathVariable("id") Long id) {
        logger.info("GET  => /customers/{}", id);
        var customerDTO = customerService.findById(id);
        return ResponseEntity.ok(customerDTO);
    }

    @GetMapping("search/{name}")
    public ResponseEntity<Page<CustomerResponseDTO>> getFindByName(
            @PathVariable String name,
            Pageable pageable) {
        logger.info("GET  => /customers/search/{name} - Request: {}", pageable);
        Page<CustomerResponseDTO> customers = customerService.findByName(name, pageable);

        return ResponseEntity.ok(customers);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> post(
            @Valid @RequestBody CustomerCreateDTO customerCreateDTO) {
        logger.info("POST => /customers - Body: {}", customerCreateDTO);

        CustomerResponseDTO customerResponseDTO = customerService.save(customerCreateDTO);

        URI uri = URI.create("/customers/" + customerResponseDTO.getId());
        return ResponseEntity.created(uri).body(customerResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> put(
            @PathVariable("id") Long id,
            @Valid @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        logger.info("PUT => /customers/{} - Body: {}", id, customerUpdateDTO);

        CustomerResponseDTO customerResponseDTO = customerService.update(customerUpdateDTO, id);
        return ResponseEntity.ok(customerResponseDTO);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody PasswordUpdateDTO updatePasswordDTO) {
        customerService.updatePassword(updatePasswordDTO, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
