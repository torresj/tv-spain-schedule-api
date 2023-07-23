package com.jtcoding.tvspainscheduleapi.repositories;

import com.jtcoding.tvspainscheduleapi.entities.ChapterEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends CrudRepository<ChapterEntity, Long> {
  Optional<ChapterEntity> findBySerieIdAndChapterName(long serieId, String chapterName);
}
