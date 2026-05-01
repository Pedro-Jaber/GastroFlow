package br.com.group14.gastroflow.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.group14.gastroflow.dtos.create.CustomerCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.CustomerResponseDTO;
import br.com.group14.gastroflow.dtos.update.CustomerUpdateDTO;
import br.com.group14.gastroflow.dtos.update.PasswordUpdateDTO;
import br.com.group14.gastroflow.entities.user.Customer;
import br.com.group14.gastroflow.repositories.CustomerRepository;
import br.com.group14.gastroflow.services.exceptions.ResourceNotFoundException;
import br.com.group14.gastroflow.services.exceptions.ValidationException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Page<CustomerResponseDTO> findAll(Pageable pageable) {
        var customersPage = customerRepository.findAll(pageable);
        Page<CustomerResponseDTO> customersDTOPage = customersPage.map(CustomerResponseDTO::new);
        return customersDTOPage;
    }

    public CustomerResponseDTO findById(Long id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));

        return new CustomerResponseDTO(customer);
    }

    public CustomerResponseDTO save(CustomerCreateDTO customerCreateDTO) {
        Customer customer = new Customer(customerCreateDTO);
        Customer savedCustomer = customerRepository.saveAndFlush(customer);
        return new CustomerResponseDTO(savedCustomer);
    }

    // TODO - Refactor validation to use validator (reference: claude code)
    public CustomerResponseDTO update(CustomerUpdateDTO customerUpdateDTO, Long id) {
        Customer originalCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));

        originalCustomer.updateFromDTO(customerUpdateDTO);

        // Validation
        CustomerCreateDTO customerCreateDTO = new CustomerCreateDTO(originalCustomer);

        Customer customerToSave = new Customer(customerCreateDTO);
        customerToSave.setId(id);

        customerRepository.saveAndFlush(customerToSave);
        return new CustomerResponseDTO(customerToSave);
    }

    // TODO - Refactor validation to use validator (reference: claude code)
    public void updatePassword(PasswordUpdateDTO updatePasswordDTO, Long id) {

        if (!updatePasswordDTO.newPassword().equals(updatePasswordDTO.confirmPassword())) {
            throw new ValidationException(List.of("New password and confirm password do not match"));
        }

        Customer originalCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));

        if (!updatePasswordDTO.currentPassword().equals(originalCustomer.getPassword())) {
            throw new ValidationException(List.of("Current password is incorrect"));
        }

        originalCustomer.setPassword(updatePasswordDTO.newPassword());

        // Validation
        CustomerCreateDTO customerCreateDTO = new CustomerCreateDTO(originalCustomer);

        Customer customerToSave = new Customer(customerCreateDTO);
        customerToSave.setId(id);

        customerRepository.saveAndFlush(customerToSave);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

}
