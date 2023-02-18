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

    @NotNull
    private String businesskey;

    @NotNull
    private String title;

    @NotNull
    private String imageUrl;

    @ManyToOne
    @JoinColumn( name = "photos")
    private PhotostoryEntity photostory;

    private PSImageEntity() {
    }

    public PSImageEntity(String title, String imageUrl) {
        this.businesskey = UUID.randomUUID().toString();
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
