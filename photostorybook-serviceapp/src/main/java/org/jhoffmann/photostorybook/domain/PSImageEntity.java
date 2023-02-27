package org.jhoffmann.photostorybook.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Slf4j
@Entity
@Table(name = "psimage")
public class PSImageEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    private String title;

    @NotNull
    private String imageUUID;

    @ManyToOne
    @JoinColumn( name = "photos")
    private PhotostoryEntity photostory;

    private PSImageEntity() {

    }

    public PSImageEntity(String imageUUID) {
        this.imageUUID = imageUUID;
    }

    public PSImageEntity(String title, String imageUUID) {
        this.title = title;
        this.imageUUID = imageUUID;
    }
}
