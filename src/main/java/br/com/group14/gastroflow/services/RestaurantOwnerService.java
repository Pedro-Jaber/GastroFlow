package br.com.group14.gastroflow.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.group14.gastroflow.dtos.create.RestaurantOwnerCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.RestaurantOwnerResponseDTO;
import br.com.group14.gastroflow.dtos.update.PasswordUpdateDTO;
import br.com.group14.gastroflow.dtos.update.RestaurantOwnerUpdateDTO;
import br.com.group14.gastroflow.entities.user.RestaurantOwner;
import br.com.group14.gastroflow.repositories.RestaurantOwnerRepository;
import br.com.group14.gastroflow.services.exceptions.ResourceNotFoundException;
import br.com.group14.gastroflow.services.exceptions.ValidationException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RestaurantOwnerService {

    private final RestaurantOwnerRepository restaurantOwnerRepository;

    public Page<RestaurantOwnerResponseDTO> findAll(Pageable pageable) {
        var restaurantOwnersPage = restaurantOwnerRepository.findAll(pageable);
        Page<RestaurantOwnerResponseDTO> restaurantOwnersDTOPage = restaurantOwnersPage
                .map(RestaurantOwnerResponseDTO::new);
        return restaurantOwnersDTOPage;
    }

    public RestaurantOwnerResponseDTO findById(Long id) {
        var restaurantOwner = restaurantOwnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantOwner Not Found"));

        return new RestaurantOwnerResponseDTO(restaurantOwner);
    }

    public RestaurantOwnerResponseDTO save(RestaurantOwnerCreateDTO restaurantOwnerCreateDTO) {
        RestaurantOwner restaurantOwner = new RestaurantOwner(restaurantOwnerCreateDTO);
        RestaurantOwner savedRestaurantOwner = restaurantOwnerRepository.saveAndFlush(restaurantOwner);
        return new RestaurantOwnerResponseDTO(savedRestaurantOwner);
    }

    // TODO - Refactor validation to use validator (reference: claude code)
    public RestaurantOwnerResponseDTO update(RestaurantOwnerUpdateDTO restaurantOwnerUpdateDTO, Long id) {
        RestaurantOwner originalRestaurantOwner = restaurantOwnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantOwner Not Found"));

        originalRestaurantOwner.updateFromDTO(restaurantOwnerUpdateDTO);

        // Validation
        RestaurantOwnerCreateDTO restaurantOwnerCreateDTO = new RestaurantOwnerCreateDTO(originalRestaurantOwner);

        RestaurantOwner restaurantOwnerToSave = new RestaurantOwner(restaurantOwnerCreateDTO);
        restaurantOwnerToSave.setId(id);

        restaurantOwnerRepository.saveAndFlush(restaurantOwnerToSave);
        return new RestaurantOwnerResponseDTO(restaurantOwnerToSave);
    }

    // TODO - Refactor validation to use validator (reference: claude code)
    public void updatePassword(PasswordUpdateDTO updatePasswordDTO, Long id) {

        if (!updatePasswordDTO.newPassword().equals(updatePasswordDTO.confirmPassword())) {
            throw new ValidationException(List.of("New password and confirm password do not match"));
        }

        RestaurantOwner originalRestaurantOwner = restaurantOwnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RestaurantOwner Not Found"));

        if (!updatePasswordDTO.currentPassword().equals(originalRestaurantOwner.getPassword())) {
            throw new ValidationException(List.of("Current password is incorrect"));
        }

        originalRestaurantOwner.setPassword(updatePasswordDTO.newPassword());

        // Validation
        RestaurantOwnerCreateDTO restaurantOwnerCreateDTO = new RestaurantOwnerCreateDTO(originalRestaurantOwner);

        RestaurantOwner restaurantOwnerToSave = new RestaurantOwner(restaurantOwnerCreateDTO);
        restaurantOwnerToSave.setId(id);

        restaurantOwnerRepository.saveAndFlush(restaurantOwnerToSave);
    }

    public void delete(Long id) {
        restaurantOwnerRepository.deleteById(id);
    }

}
