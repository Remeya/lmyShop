package com.lmy.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    private String name;
    private String subTitle;
    private Double originalPrice;
    private Double promotePrice;
    private int stock;
    private String createDate;
    @ManyToOne
    @Column(name = "cid")
    private Category category;
}
