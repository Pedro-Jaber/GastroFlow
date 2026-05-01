package br.com.group14.gastroflow.services;

import org.springframework.stereotype.Service;

import br.com.group14.gastroflow.dtos.create.CustomerCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.CustomerResponseDTO;
import br.com.group14.gastroflow.dtos.update.CustomerUpdateDTO;
import br.com.group14.gastroflow.entities.user.Customer;
import br.com.group14.gastroflow.repositories.CustomerRepository;

@Service
public class CustomerService extends
        UserBaseService<Customer, CustomerRepository, CustomerResponseDTO, CustomerCreateDTO, CustomerUpdateDTO> {

    public CustomerService(CustomerRepository repository) {
        super(repository, "Customer");
    }

    @Override
    protected CustomerResponseDTO convertToResponseDTO(Customer entity) {
        return new CustomerResponseDTO(entity);
    }

    @Override
    protected Customer convertToEntity(CustomerCreateDTO createDTO) {
        return new Customer(createDTO);
    }

    @Override
    protected CustomerCreateDTO convertToCreateDTO(Customer entity) {
        return new CustomerCreateDTO(entity);
    }

}
