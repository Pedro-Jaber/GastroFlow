package br.com.group14.gastroflow.dtos.reponse;

import br.com.group14.gastroflow.dtos.AddressDTO;
import br.com.group14.gastroflow.entities.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBaseResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String login;
    private AddressDTO address;

    public UserBaseResponseDTO(UserBase<?> userBase) {
        this(
                userBase.getId(),
                userBase.getName(),
                userBase.getEmail(),
                userBase.getLogin(),
                userBase.getAddress() != null ? new AddressDTO(userBase.getAddress()) : null);
    }

}
