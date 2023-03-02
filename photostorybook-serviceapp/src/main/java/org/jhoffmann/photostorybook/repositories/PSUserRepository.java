package org.jhoffmann.photostorybook.repositories;

import org.jhoffmann.photostorybook.domain.PSUser;
import org.springframework.data.repository.CrudRepository;

public interface PSUserRepository extends CrudRepository<PSUser, Long> {

    PSUser findByUsername(String username);

}
