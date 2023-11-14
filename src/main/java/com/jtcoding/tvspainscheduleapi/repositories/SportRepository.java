package com.jtcoding.tvspainscheduleapi.repositories;

import com.jtcoding.tvspainscheduleapi.entities.SportEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends CrudRepository<SportEntity, Long> {
}
