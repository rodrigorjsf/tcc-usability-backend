package com.unicap.tcc.usability.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicap.tcc.usability.api.models.enums.CategoriesEnum;
import com.unicap.tcc.usability.api.models.enums.UserProfileEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "uuid default uuid_generate_v4()", insertable = false)
    private UUID uid;

    @Column
    @NotEmpty(message = "Campo login é obrigatório.")
    private String login;

    @Column
    @NotEmpty(message = "Campo password é obrigatório.")
    private String password;

    @Column
    private String name;

    @Column
    @NotEmpty(message = "Campo e-mail é obrigatório.")
    private String email;

    @Column(columnDefinition = "boolean default false")
    private Boolean admin;

    @Column(columnDefinition = "boolean default false")
    private boolean isEnabled;

    @Column(columnDefinition = "boolean default false")
    private boolean isReviewer;


    public Boolean isAdmin() {
        return this.admin;
    }
}
