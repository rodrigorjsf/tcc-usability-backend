package com.unicap.tcc.usability.api.models.review;


import com.unicap.tcc.usability.api.models.enums.SectionEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private SectionEnum section;
    private String comment;
}
