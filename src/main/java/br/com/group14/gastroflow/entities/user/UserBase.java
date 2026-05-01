package br.com.group14.gastroflow.entities.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import br.com.group14.gastroflow.dtos.create.UserBaseCreateDTO;
import br.com.group14.gastroflow.dtos.update.UserBaseUpdateDTO;
import br.com.group14.gastroflow.entities.Address;
import br.com.group14.gastroflow.interfaces.updatableFromDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class UserBase<U extends UserBaseUpdateDTO> implements updatableFromDTO<U> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @NotBlank(message = "User must have attribute name")
  @Column(nullable = false)
  protected String name;

  @Email(message = "User email must be a valid email address")
  @NotBlank(message = "User must have attribute email")
  @Column(unique = true, nullable = false)
  protected String email;

  @NotBlank(message = "User must have attribute login")
  @Column(unique = true, nullable = false)
  protected String login;

  @NotBlank(message = "User must have attribute password")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  @Column(nullable = false)
  protected String password;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  protected Address address;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  public UserBase(UserBaseCreateDTO userBaseCreateDTO) {
    this(
        null,
        userBaseCreateDTO.getName(),
        userBaseCreateDTO.getEmail(),
        userBaseCreateDTO.getLogin(),
        userBaseCreateDTO.getPassword(),
        userBaseCreateDTO.getAddress() != null ? new Address(userBaseCreateDTO.getAddress()) : null,
        null);
  }

  public UserBase(UserBaseUpdateDTO userBaseUpdateDTO) {
    this(
        null,
        userBaseUpdateDTO.getName(),
        userBaseUpdateDTO.getEmail(),
        userBaseUpdateDTO.getLogin(),
        null,
        userBaseUpdateDTO.getAddress() != null ? new Address(userBaseUpdateDTO.getAddress()) : null,
        null);
  }

  public void baseUpdateFromDTO(UserBaseUpdateDTO userBaseUpdateDTO) {

    if (userBaseUpdateDTO.getName() != null)
      this.name = userBaseUpdateDTO.getName();
    if (userBaseUpdateDTO.getEmail() != null)
      this.email = userBaseUpdateDTO.getEmail();
    if (userBaseUpdateDTO.getLogin() != null)
      this.login = userBaseUpdateDTO.getLogin();

    if (userBaseUpdateDTO.getAddress() != null) {
      if (this.address == null) {
        this.address = new Address(userBaseUpdateDTO.getAddress());
      } else {
        this.address.updateFromDTO(userBaseUpdateDTO.getAddress());
      }
    }
  }

}
