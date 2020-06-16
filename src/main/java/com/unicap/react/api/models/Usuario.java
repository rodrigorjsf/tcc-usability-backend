package com.unicap.react.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.DefaultValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column
    @NotEmpty(message = "Campo login é obrigatório.")
    private String login;
    @Column
    @NotEmpty(message = "Campo senha é obrigatório.")
    private String senha;
    @Column
    @NotEmpty(message = "Campo e-mail é obrigatório.")
    private String email;
    @Column
    @DefaultValue(value = "false")
    private Boolean admin;

    public Boolean isAdmin () {
        return this.admin;
    }
}
