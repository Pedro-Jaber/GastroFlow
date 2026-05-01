package br.com.group14.gastroflow.dtos.create;

import br.com.group14.gastroflow.dtos.AddressDTO;
import br.com.group14.gastroflow.entities.user.UserBase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseCreateDTO {
  @NotBlank(message = "User must have attribute name")
  protected String name;

  @Email
  @NotBlank(message = "User must have attribute email")
  protected String email;

  @NotBlank(message = "User must have attribute login")
  protected String login;

  @NotBlank(message = "User must have attribute password")
  // TODO: Add password strength validation
  protected String password;

  @Valid
  protected AddressDTO address;

  public UserBaseCreateDTO(UserBase user) {
    this(user.getName(), user.getEmail(), user.getLogin(), user.getPassword(),
        new AddressDTO(user.getAddress()));
  }

}
