package org.jhoffmann.photostorybook.repositories;

import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PhotostoryRepository extends JpaRepository<PhotostoryEntity, Long> {
    List<PhotostoryEntity> findByBusinesskey(String businesskey);

}
