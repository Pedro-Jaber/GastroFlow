package br.com.group14.gastroflow.dtos.reponse;

import br.com.group14.gastroflow.entities.user.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerResponseDTO extends UserBaseResponseDTO {

    private String cpf;
    private String phone;

    public CustomerResponseDTO(Customer customer) {
        super(customer);
        this.cpf = customer.getCpf();
        this.phone = customer.getPhone();
    }
}
