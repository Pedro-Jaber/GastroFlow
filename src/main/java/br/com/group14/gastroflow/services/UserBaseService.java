package br.com.group14.gastroflow.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.group14.gastroflow.dtos.create.UserBaseCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.UserBaseResponseDTO;
import br.com.group14.gastroflow.dtos.update.PasswordUpdateDTO;
import br.com.group14.gastroflow.dtos.update.UserBaseUpdateDTO;
import br.com.group14.gastroflow.entities.user.UserBase;
import br.com.group14.gastroflow.services.exceptions.ResourceNotFoundException;
import br.com.group14.gastroflow.services.exceptions.ValidationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class UserBaseService<E extends UserBase, R extends JpaRepository<E, Long>, ResponseDTO extends UserBaseResponseDTO, CreateDTO extends UserBaseCreateDTO, UpdateDTO extends UserBaseUpdateDTO> {

    protected final R repository;
    private final String entityName;

    // * Abstract methods

    protected abstract ResponseDTO convertToResponseDTO(E entity);

    protected abstract E convertToEntity(CreateDTO createDTO);

    protected abstract CreateDTO convertToCreateDTO(E entity);

    // * Common methods

    public Page<ResponseDTO> findAll(Pageable pageable) {
        var usersPaged = repository.findAll(pageable);
        Page<ResponseDTO> usersDTOPage = usersPaged.map(this::convertToResponseDTO);
        return usersDTOPage;
    }

    public ResponseDTO findById(Long id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " Not Found"));

        return convertToResponseDTO(user);
    }

    public ResponseDTO save(CreateDTO createDTO) {
        E user = convertToEntity(createDTO);
        E savedUser = repository.saveAndFlush(user);
        return convertToResponseDTO(savedUser);
    }

    public ResponseDTO update(UpdateDTO updateDTO, Long id) {
        E user = findOrThrow(id);
        user.updateFromDTO(updateDTO);

        // Validation
        E userToSave = convertToEntity(convertToCreateDTO(user));
        userToSave.setId(id);

        E savedUser = repository.saveAndFlush(userToSave);
        return convertToResponseDTO(savedUser);
    }

    public void updatePassword(PasswordUpdateDTO dto, Long id) {
        if (!dto.newPassword().equals(dto.confirmPassword())) {
            throw new ValidationException(List.of("New password and confirm password do not match"));
        }

        E entity = findOrThrow(id);

        if (!dto.currentPassword().equals(entity.getPassword())) {
            throw new ValidationException(List.of("Current password is incorrect"));
        }

        entity.setPassword(dto.newPassword());

        E toSave = convertToEntity(convertToCreateDTO(entity));
        toSave.setId(id);
        repository.saveAndFlush(toSave);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // * Utils

    protected E findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " Not Found"));
    }

}
