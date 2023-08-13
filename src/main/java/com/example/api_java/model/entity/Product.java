package com.example.api_java.model.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

// Lombok
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "feature_detail",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id"))
    List<Feature> features;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date productCreateDate;

    @Column(name = "price")
    @DecimalMin(value = "0", message = "Product price must greater than 0")
    @NotNull
    private BigDecimal productPrice;

    @Column(name = "description")
    private String productDescription;

    @Column(name = "name")
    @NotNull(message = "Product name must not be null")
    @NotBlank(message = "Product name must not be blank")
    private String productName;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date productUpDate;

    @Column(name = "remain")
    @DecimalMin(value = "0", message = "Product remain must greater than 0")
    private Integer productRemain;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id") // Tên cột join trong db
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id") // Tên cột join trong db
    private Event event;

    @Transient
    @OneToMany(fetch = FetchType.LAZY)
    private List<Image> images;

    @Transient
    @OneToMany(fetch = FetchType.LAZY)
    private List<Rate> rates;
}
