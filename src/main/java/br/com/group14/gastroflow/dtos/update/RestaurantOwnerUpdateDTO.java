package br.com.group14.gastroflow.dtos.update;

import br.com.group14.gastroflow.dtos.AddressDTO;
import br.com.group14.gastroflow.entities.user.RestaurantOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RestaurantOwnerUpdateDTO extends UserBaseUpdateDTO {
    private String cpfCnpj;
    private String phone;

    public RestaurantOwnerUpdateDTO(RestaurantOwner restaurantOwner) {
        super(restaurantOwner.getName(), restaurantOwner.getEmail(), restaurantOwner.getLogin(),
                new AddressDTO(restaurantOwner.getAddress()));
        this.cpfCnpj = restaurantOwner.getCpfCnpj();
        this.phone = restaurantOwner.getPhone();
    }
}
