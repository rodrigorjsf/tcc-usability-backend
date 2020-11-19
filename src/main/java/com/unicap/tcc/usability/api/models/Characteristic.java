package com.unicap.tcc.usability.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "characteristic")
public class Characteristic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category", columnDefinition = "varchar(2)")
    private String category;
    @Column(name = "key", columnDefinition = "varchar(3)")
    private String key;
    @Column(name = "description", columnDefinition = "text")
    private String description;
}
