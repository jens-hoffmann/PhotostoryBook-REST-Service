package org.jhoffmann.photostorybookwebclient.util;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewPhotostoryForm {
    @NotEmpty
    private String storyTitle;

    @NotNull
    private String titleImage;

    public NewPhotostoryForm(String storyTitle, String titleImage) {
        this.storyTitle = storyTitle;
        this.titleImage = titleImage;
    }
}
