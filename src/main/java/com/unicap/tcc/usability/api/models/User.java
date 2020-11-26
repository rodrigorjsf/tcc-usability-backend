package com.unicap.tcc.usability.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_user")
public class User extends BaseEntity implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "uuid default uuid_generate_v4()")
    private UUID uid;

    @JsonIgnore
    @Column
    @NotEmpty(message = "Campo login é obrigatório.")
    private String login;

    @JsonIgnore
    @Column
    @NotEmpty(message = "Campo password é obrigatório.")
    private String password;

    @Column
    private String name;

    @JsonIgnore
    @Column
    @NotEmpty(message = "Campo e-mail é obrigatório.")
    private String email;

    @JsonIgnore
    @Column(columnDefinition = "boolean default false")
    private Boolean admin;

    @JsonIgnore
    @Column(columnDefinition = "boolean default false")
    private Boolean isEnabled;

    @JsonIgnore
    @Column(columnDefinition = "boolean default false")
    private Boolean isReviewer;


    public Boolean isAdmin() {
        return this.admin;
    }
}
