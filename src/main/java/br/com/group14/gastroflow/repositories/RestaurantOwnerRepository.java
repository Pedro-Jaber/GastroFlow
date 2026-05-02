package br.com.group14.gastroflow.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.group14.gastroflow.entities.user.RestaurantOwner;

@Repository
public interface RestaurantOwnerRepository extends UserBaseRepository<RestaurantOwner> {

    @Override
    @Query("SELECT u FROM RestaurantOwner u WHERE u.name ILIKE CONCAT('%', :name, '%')")
    Page<RestaurantOwner> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
