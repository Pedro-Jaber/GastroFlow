package br.com.group14.gastroflow.dtos.create;

import br.com.group14.gastroflow.dtos.AddressDTO;
import br.com.group14.gastroflow.entities.user.RestaurantOwner;
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
public class RestaurantOwnerCreateDTO extends UserBaseCreateDTO {
    @NotBlank(message = "Restaurant owner must have attribute cpfCnpj")
    @Size(min = 11, max = 14, message = "CPF/CNPJ must be between 11 and 14 characters long")
    private String cpfCnpj;
    private String phone;

    public RestaurantOwnerCreateDTO(RestaurantOwner restaurantOwner) {
        super(
                restaurantOwner.getName(),
                restaurantOwner.getEmail(),
                restaurantOwner.getLogin(),
                restaurantOwner.getPassword(),
                restaurantOwner.getAddress() != null ? new AddressDTO(restaurantOwner.getAddress()) : null);
        this.cpfCnpj = restaurantOwner.getCpfCnpj();
        this.phone = restaurantOwner.getPhone();
    }
}
