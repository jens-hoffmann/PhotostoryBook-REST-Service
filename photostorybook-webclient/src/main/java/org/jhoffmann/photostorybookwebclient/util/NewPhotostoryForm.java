package org.jhoffmann.photostorybookwebclient.util;

import lombok.Data;
import okhttp3.MultipartBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewPhotostoryForm {
    @NotEmpty
    private String storyTitle;

    @NotNull
    private MultipartFile titleImage;

    public NewPhotostoryForm(String storyTitle, MultipartFile titleImage) {
        this.storyTitle = storyTitle;
        this.titleImage = titleImage;
    }
}
