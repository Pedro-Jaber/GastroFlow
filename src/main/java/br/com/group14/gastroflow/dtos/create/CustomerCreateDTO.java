package br.com.group14.gastroflow.dtos.create;

import br.com.group14.gastroflow.dtos.AddressDTO;
import br.com.group14.gastroflow.entities.user.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomerCreateDTO extends UserBaseCreateDTO {
    @NotBlank(message = "Client must have attribute cpf")
    @Size(min = 11, max = 11, message = "CPF must be exactly 11 characters long")
    private String cpf;
    private String phone;

    public CustomerCreateDTO(Customer customer) {
        super(
                customer.getName(),
                customer.getEmail(),
                customer.getLogin(),
                customer.getPassword(),
                customer.getAddress() != null ? new AddressDTO(customer.getAddress()) : null);
        this.cpf = customer.getCpf();
        this.phone = customer.getPhone();
    }
}
