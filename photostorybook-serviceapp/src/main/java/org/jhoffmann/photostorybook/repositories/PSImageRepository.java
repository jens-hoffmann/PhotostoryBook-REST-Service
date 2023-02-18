package org.jhoffmann.photostorybook.repositories;

import org.jhoffmann.photostorybook.domain.PSImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PSImageRepository extends JpaRepository<PSImageEntity, Long> {
}
