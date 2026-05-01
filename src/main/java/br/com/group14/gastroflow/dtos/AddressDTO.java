package br.com.group14.gastroflow.dtos;

import br.com.group14.gastroflow.entities.Address;
import jakarta.validation.constraints.Size;

public record AddressDTO(
        String street,
        String number,
        String city,
        @Size(min = 8, max = 8, message = "CEP must be exactly 8 characters long") String cep,
        String complement,
        String state

) {

    public AddressDTO(Address address) {
        this(address.getStreet(), address.getNumber(), address.getCity(), address.getCep(),
                address.getComplement(), address.getState());
    }
}
