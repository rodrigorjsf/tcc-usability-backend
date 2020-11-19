package com.unicap.tcc.usability.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "sc_questionnaire")
public class SmartCityQuestionnaire extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "has_data_management", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean hasDataManagement;
    @Column(name = "has_app_execution", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean hasAppExecution;
    @Column(name = "has_sensor_network", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean hasSensorNetwork;
    @Column(name = "has_data_processing", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean hasDataProcessing;
    @Column(name = "has_data_access", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean hasDataAccess;
    @Column(name = "has_service_management", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean hasServiceManagement;
    @Column(name = "has_software_tools", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean hasSoftwareTools;
    @Column(name = "define_city_model", columnDefinition = "boolean default false")
    @Generated(GenerationTime.ALWAYS)
    private Boolean defineCityModel;

    public List<Boolean> getListOfResults(){
        return new ArrayList<>(Arrays.asList(hasDataManagement,hasAppExecution,hasSensorNetwork,
                hasDataProcessing, hasDataAccess, hasServiceManagement,hasSoftwareTools, defineCityModel));
    }
}


