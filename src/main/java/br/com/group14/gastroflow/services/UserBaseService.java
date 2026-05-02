package br.com.group14.gastroflow.services;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.group14.gastroflow.dtos.create.UserBaseCreateDTO;
import br.com.group14.gastroflow.dtos.reponse.UserBaseResponseDTO;
import br.com.group14.gastroflow.dtos.update.PasswordUpdateDTO;
import br.com.group14.gastroflow.dtos.update.UserBaseUpdateDTO;
import br.com.group14.gastroflow.entities.user.UserBase;
import br.com.group14.gastroflow.repositories.UserBaseRepository;
import br.com.group14.gastroflow.services.exceptions.ResourceNotFoundException;
import br.com.group14.gastroflow.services.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class UserBaseService<E extends UserBase<UpdateDTO>, R extends UserBaseRepository<E>, ResponseDTO extends UserBaseResponseDTO, CreateDTO extends UserBaseCreateDTO, UpdateDTO extends UserBaseUpdateDTO> {

    // * Attributes
    protected final R repository;
    private final String entityName;
    private final Validator validator;

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

    public Page<ResponseDTO> findByName(String name, Pageable pageable) {
        Page<E> usersPaged = repository.findByNameContainingIgnoreCase(name, pageable);
        Page<ResponseDTO> usersDTOPage = usersPaged.map(this::convertToResponseDTO);
        return usersDTOPage;
    }

    public ResponseDTO save(CreateDTO createDTO) {
        E user = convertToEntity(createDTO);
        E savedUser = repository.saveAndFlush(user);
        return convertToResponseDTO(savedUser);
    }

    public ResponseDTO update(UpdateDTO updateDTO, Long id) {
        E user = findOrThrow(id);
        user.updateFromDTO(updateDTO);

        validate(user);

        E savedUser = repository.saveAndFlush(user);
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

        validate(entity);

        repository.saveAndFlush(entity);
    }

    public void delete(Long id) {
        findOrThrow(id);

        repository.deleteById(id);
    }

    // * Utils

    protected E findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " Not Found"));
    }

    protected void validate(E entity) {
        Set<ConstraintViolation<E>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.stream().map(ConstraintViolation::getMessage).toList());
        }
    }
}
