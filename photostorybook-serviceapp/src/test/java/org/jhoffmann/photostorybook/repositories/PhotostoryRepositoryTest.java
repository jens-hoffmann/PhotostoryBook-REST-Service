package org.jhoffmann.photostorybook.repositories;

import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class PhotostoryRepositoryTest {

    @Autowired
    private PhotostoryRepository photostoryRepository;

    @Test
    public void findAllGivesNonEmptyList() {
        final List<PhotostoryEntity> photostories = photostoryRepository.findAll();
        assertThat(photostories.size()).isGreaterThan(0);
    }

}
