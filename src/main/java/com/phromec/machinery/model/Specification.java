package com.phromec.machinery.model;

import com.phromec.machinery.model.part.PartSpecification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "specifications")
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specification_id")
    private Integer specificationId;
    @Column(name = "specification_name", nullable = false, unique = true, length = 100)
    private String specificationName;
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", length = 7)
    private SpecificationDataType dataType = SpecificationDataType.TEXT;
    @Column(length = 30)
    private String unit;
    @Column(length = 255)
    private String description;
    @OneToMany(mappedBy = "specification")
    private List<PartSpecification> partSpecifications = new ArrayList<>();

    public Specification() {
    }

}
