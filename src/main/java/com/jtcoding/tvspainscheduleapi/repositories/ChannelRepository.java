package com.jtcoding.tvspainscheduleapi.repositories;

import com.jtcoding.tvspainscheduleapi.entities.ChannelEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends CrudRepository<ChannelEntity, Long> {
    List<ChannelEntity> findByOrderByNameDesc();
}
