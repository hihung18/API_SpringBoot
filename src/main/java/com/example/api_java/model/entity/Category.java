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
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long cateId;

    @Column(name = "cate_name")
    private String categoryName;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category") // Tên thuộc tính join
    private List<Product> products;
}
