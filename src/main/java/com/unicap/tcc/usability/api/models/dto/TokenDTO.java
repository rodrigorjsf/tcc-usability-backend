package com.unicap.tcc.usability.api.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String login;
    private String token;
    private Collection<? extends GrantedAuthority> roles;

}
