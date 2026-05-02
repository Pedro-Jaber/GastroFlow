package br.com.group14.gastroflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.group14.gastroflow.entities.user.UserBase;

@Repository
public interface UserBaseRepository<U extends UserBase<?>> extends JpaRepository<U, Long> {

    U findByLogin(String login);

    @Query("SELECT u FROM UserBase u WHERE u.name ILIKE CONCAT('%', :name, '%')")
    Page<U> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
