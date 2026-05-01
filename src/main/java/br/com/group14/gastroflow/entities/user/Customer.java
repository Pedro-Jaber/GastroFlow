package br.com.group14.gastroflow.entities.user;

import br.com.group14.gastroflow.dtos.create.CustomerCreateDTO;
import br.com.group14.gastroflow.dtos.update.CustomerUpdateDTO;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
public class Customer extends UserBase<CustomerUpdateDTO> {

    @NotBlank(message = "Client must have attribute cpf")
    @Size(min = 11, max = 11, message = "CPF must be exactly 11 characters long")
    @Column(unique = true)
    private String cpf;

    private String phone;

    public Customer(CustomerCreateDTO customerCreateDTO) {
        super(customerCreateDTO);
        this.cpf = customerCreateDTO.getCpf();
        this.phone = customerCreateDTO.getPhone();
    }

    public Customer(CustomerUpdateDTO customerUpdateDTO) {
        super(customerUpdateDTO);
        this.cpf = customerUpdateDTO.getCpf();
        this.phone = customerUpdateDTO.getPhone();
    }

    @Override
    public void updateFromDTO(CustomerUpdateDTO customerUpdateDTO) {
        super.baseUpdateFromDTO(customerUpdateDTO);

        if (customerUpdateDTO.getCpf() != null)
            this.cpf = customerUpdateDTO.getCpf();
        if (customerUpdateDTO.getPhone() != null)
            this.phone = customerUpdateDTO.getPhone();
    }
}
