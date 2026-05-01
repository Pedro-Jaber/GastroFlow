package br.com.group14.gastroflow.dtos.update;

import br.com.group14.gastroflow.dtos.AddressDTO;
import br.com.group14.gastroflow.entities.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseUpdateDTO {
  protected String name;
  protected String email;
  protected String login;
  protected AddressDTO address;

  public UserBaseUpdateDTO(UserBase user) {
    this(user.getName(), user.getEmail(), user.getLogin(),
        new AddressDTO(user.getAddress()));
  }

}
