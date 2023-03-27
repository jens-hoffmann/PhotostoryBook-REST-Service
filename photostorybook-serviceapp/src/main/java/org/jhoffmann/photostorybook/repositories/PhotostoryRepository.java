package org.jhoffmann.photostorybook.repositories;

import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PhotostoryRepository extends JpaRepository<PhotostoryEntity, Long> {
    List<PhotostoryEntity> findByBusinesskeyAndUserId(String businesskey, String userId);
    List<PhotostoryEntity> findByUserId(String userId);

}
