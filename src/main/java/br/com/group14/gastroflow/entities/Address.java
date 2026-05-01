package br.com.group14.gastroflow.entities;

import br.com.group14.gastroflow.dtos.AddressDTO;
import br.com.group14.gastroflow.interfaces.updatableFromDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address implements updatableFromDTO<AddressDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String number;
    private String city;
    private String cep;
    private String complement;
    private String state;

    public Address(AddressDTO addressDTO) {
        this(
                null,
                addressDTO.street(),
                addressDTO.number(),
                addressDTO.city(),
                addressDTO.cep(),
                addressDTO.complement(),
                addressDTO.state());
    }

    @Override
    public void updateFromDTO(AddressDTO addressDTO) {
        if (addressDTO.street() != null)
            this.street = addressDTO.street();
        if (addressDTO.number() != null)
            this.number = addressDTO.number();
        if (addressDTO.city() != null)
            this.city = addressDTO.city();
        if (addressDTO.cep() != null)
            this.cep = addressDTO.cep();
        if (addressDTO.complement() != null)
            this.complement = addressDTO.complement();
        if (addressDTO.state() != null)
            this.state = addressDTO.state();
    }

}
