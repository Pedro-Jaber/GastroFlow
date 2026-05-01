package br.com.group14.gastroflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.group14.gastroflow.entities.user.UserBase;

@Repository
public interface UserBaseRepository extends JpaRepository<UserBase, Long> {

}
