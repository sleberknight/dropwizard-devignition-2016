package com.devignition.service.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Wither
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
