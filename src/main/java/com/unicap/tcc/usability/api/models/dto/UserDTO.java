package com.unicap.tcc.usability.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String login;
    private String email;
    private boolean admin;
    private boolean isEnable;
}
