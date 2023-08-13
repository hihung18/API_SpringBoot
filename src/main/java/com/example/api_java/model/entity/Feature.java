package com.example.api_java.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "features")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private Long featureFeatureId;

    @Column(name = "specific")
    private String featureSpecific;

    @Column(name = "point")
    private Long featurePoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "featureType_id")
    private FeatureType featureType;
}
