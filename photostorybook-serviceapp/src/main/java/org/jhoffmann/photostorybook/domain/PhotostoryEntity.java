package org.jhoffmann.photostorybook.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Slf4j
@Entity
@Table(name = "photostory")
public class PhotostoryEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @NotNull
    private String businesskey;

    @NotNull
    private String userId;

    @NotNull
    private String storyTitle;

    @OneToOne
    @JoinColumn( name = "image_fk" )
    private PSImageEntity titleImage;

    @OneToMany(
            mappedBy = "photostory",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PSImageEntity> photos;

    private PhotostoryEntity() {
    }

    public PhotostoryEntity(String storyTitle, String userId) {
        this.storyTitle = storyTitle;
        this.userId = userId;
        this.businesskey = UUID.randomUUID().toString();
    }
}
