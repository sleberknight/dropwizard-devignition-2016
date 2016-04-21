package com.devignition.service.core;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@ToString
@Builder
public class Speaker {

    private Long id;

    @NotBlank
    @Length(max = 256)
    private String name;

    @Length(max = 256)
    private String twitterHandle;

    @NotBlank
    @Length(max = 65536)
    private String bio;

    @Length(max = 1024)
    private String talkTitle;

    @Length(max = 65536)
    private String talkAbstract;

}
