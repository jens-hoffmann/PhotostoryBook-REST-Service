package org.jhoffmann.photostorybook.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    private String userId;

    @NotNull
    private String businesskey;
    @NotNull
    private String imageUrl;

    @ManyToOne
    @JoinColumn( name = "photos")
    private PhotostoryEntity photostory;

    private PSImageEntity() {

    }

    public PSImageEntity(String imageUrl, String businesskey, String userId) {
        this.imageUrl = imageUrl;
        this.businesskey = businesskey;
        this.userId = userId;

    }

    public PSImageEntity(String title, String imageUrl, String businesskey, String userId) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.businesskey = businesskey;
        this.userId = userId;
    }
}
