package com.example.api_java.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "featuretype")
public class FeatureType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long featureTypeId;

    @Column(name = "name")
    private String featureTypeName;

    @Column(name = "unit")
    private String featureTypeUnit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "featureType")
    private List<Feature> features;
}
