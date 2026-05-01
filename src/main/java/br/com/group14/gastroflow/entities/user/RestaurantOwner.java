package br.com.group14.gastroflow.entities.user;

import br.com.group14.gastroflow.dtos.create.RestaurantOwnerCreateDTO;
import br.com.group14.gastroflow.dtos.update.RestaurantOwnerUpdateDTO;
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
@Table(name = "restaurant_owners")
@DiscriminatorValue("RESTAURANT_OWNER")
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
public class RestaurantOwner extends UserBase<RestaurantOwnerUpdateDTO> {

    @NotBlank(message = "Restaurant owner must have attribute cpfCnpj")
    @Size(min = 11, max = 14, message = "CPF/CNPJ must be between 11 and 14 characters long")
    @Column(name = "cpf_cnpj", unique = true)
    private String cpfCnpj;

    private String phone;

    public RestaurantOwner(RestaurantOwnerCreateDTO restaurantOwnerCreateDTO) {
        super(restaurantOwnerCreateDTO);
        this.cpfCnpj = restaurantOwnerCreateDTO.getCpfCnpj();
        this.phone = restaurantOwnerCreateDTO.getPhone();
    }

    public RestaurantOwner(RestaurantOwnerUpdateDTO restaurantOwnerUpdateDTO) {
        super(restaurantOwnerUpdateDTO);
        this.cpfCnpj = restaurantOwnerUpdateDTO.getCpfCnpj();
        this.phone = restaurantOwnerUpdateDTO.getPhone();
    }

    @Override
    public void updateFromDTO(RestaurantOwnerUpdateDTO restaurantOwnerUpdateDTO) {
        super.baseUpdateFromDTO(restaurantOwnerUpdateDTO);
        if (restaurantOwnerUpdateDTO.getCpfCnpj() != null)
            this.cpfCnpj = restaurantOwnerUpdateDTO.getCpfCnpj();
        if (restaurantOwnerUpdateDTO.getPhone() != null)
            this.phone = restaurantOwnerUpdateDTO.getPhone();
    }
}
