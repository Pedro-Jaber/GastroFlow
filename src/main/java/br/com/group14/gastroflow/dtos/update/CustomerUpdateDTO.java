package br.com.group14.gastroflow.dtos.update;

import br.com.group14.gastroflow.dtos.AddressDTO;
import br.com.group14.gastroflow.entities.user.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomerUpdateDTO extends UserBaseUpdateDTO {
    private String cpf;
    private String phone;

    public CustomerUpdateDTO(Customer customer) {
        super(customer.getName(), customer.getEmail(), customer.getLogin(), new AddressDTO(customer.getAddress()));
        this.cpf = customer.getCpf();
        this.phone = customer.getPhone();
    }
}
