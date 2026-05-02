package br.com.group14.gastroflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.group14.gastroflow.entities.user.Customer;

@Repository
public interface CustomerRepository extends UserBaseRepository<Customer> {

    @Override
    @Query("SELECT u FROM Customer u WHERE u.name ILIKE CONCAT('%', :name, '%')")
    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
