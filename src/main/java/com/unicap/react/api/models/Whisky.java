package com.unicap.react.api.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_whisky")
public class Whisky implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uuid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonProperty("alcohol")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal alcoholContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer age;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean chillFiltered;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(columnDefinition = "text")
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String distillery;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(columnDefinition = "text")
    private String flavors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String maturation;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String region;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean singleCask;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String style;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(columnDefinition = "text")
    private String tasteNotes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String vol;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(columnDefinition = "text")
    private String nose;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(columnDefinition = "text")
    private String palate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(columnDefinition = "text")
    private String finish;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String color;
    @JsonIgnore
    private LocalDateTime startDate;
    @JsonIgnore
    private LocalDateTime endDate;
    private String distilleryImageUrl;
    private String imageUrl;
}
