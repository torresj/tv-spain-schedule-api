package com.jtcoding.tvspainscheduleapi.repositories;

import com.jtcoding.tvspainscheduleapi.entities.MovieEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<MovieEntity, Long> {
  Optional<MovieEntity> findByName(String name);
  List<MovieEntity> findByRate(Double rate);
}
