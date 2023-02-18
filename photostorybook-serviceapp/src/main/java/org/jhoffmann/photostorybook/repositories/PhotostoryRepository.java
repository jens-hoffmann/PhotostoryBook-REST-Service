package org.jhoffmann.photostorybook.repositories;

import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PhotostoryRepository extends JpaRepository<PhotostoryEntity, Long> {

}
