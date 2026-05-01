package br.com.group14.gastroflow.services;

import org.springframework.stereotype.Service;

import br.com.group14.gastroflow.dtos.create.RestaurantOwnerCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.RestaurantOwnerResponseDTO;
import br.com.group14.gastroflow.dtos.update.RestaurantOwnerUpdateDTO;
import br.com.group14.gastroflow.entities.user.RestaurantOwner;
import br.com.group14.gastroflow.repositories.RestaurantOwnerRepository;
import jakarta.validation.Validator;

@Service
public class RestaurantOwnerService extends
        UserBaseService<RestaurantOwner, RestaurantOwnerRepository, RestaurantOwnerResponseDTO, RestaurantOwnerCreateDTO, RestaurantOwnerUpdateDTO> {

    public RestaurantOwnerService(RestaurantOwnerRepository repository, Validator validator) {
        super(repository, "Restaurant Owner", validator);
    }

    @Override
    protected RestaurantOwnerResponseDTO convertToResponseDTO(RestaurantOwner entity) {
        return new RestaurantOwnerResponseDTO(entity);
    }

    @Override
    protected RestaurantOwner convertToEntity(RestaurantOwnerCreateDTO createDTO) {
        return new RestaurantOwner(createDTO);
    }

    @Override
    protected RestaurantOwnerCreateDTO convertToCreateDTO(RestaurantOwner entity) {
        return new RestaurantOwnerCreateDTO(entity);
    }

}
