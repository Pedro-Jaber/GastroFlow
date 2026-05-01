package br.com.group14.gastroflow.dtos.reponse;

import br.com.group14.gastroflow.entities.user.RestaurantOwner;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RestaurantOwnerResponseDTO extends UserBaseResponseDTO {

    private String cpfCnpj;
    private String phone;

    public RestaurantOwnerResponseDTO(RestaurantOwner restaurantOwner) {
        super(restaurantOwner);
        this.cpfCnpj = restaurantOwner.getCpfCnpj();
        this.phone = restaurantOwner.getPhone();
    }

}
