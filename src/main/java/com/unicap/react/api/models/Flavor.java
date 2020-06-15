package com.unicap.react.api.models;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_flavor")
public class Flavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


}
