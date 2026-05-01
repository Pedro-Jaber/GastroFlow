package br.com.group14.gastroflow.entities.user;

import br.com.group14.gastroflow.dtos.create.CustomerCreateDTO;
import br.com.group14.gastroflow.dtos.update.CustomerUpdateDTO;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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
public class Customer extends UserBase {

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

    public void updateFromDTO(CustomerUpdateDTO customerUpdateDTO) {
        super.updateFromDTO(customerUpdateDTO);

        if (customerUpdateDTO.getCpf() != null)
            this.cpf = customerUpdateDTO.getCpf();
        if (customerUpdateDTO.getPhone() != null)
            this.phone = customerUpdateDTO.getPhone();
    }
}
