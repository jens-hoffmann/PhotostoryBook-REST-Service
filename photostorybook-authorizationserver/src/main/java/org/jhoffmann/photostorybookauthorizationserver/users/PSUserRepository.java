package org.jhoffmann.photostorybookauthorizationserver.users;

import org.springframework.data.repository.CrudRepository;

public interface PSUserRepository extends CrudRepository<PSUser, Long> {

    PSUser findByUsername(String username);

}
