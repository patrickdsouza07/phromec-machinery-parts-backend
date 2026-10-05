package com.phromec.management.model;
import jakarta.persistence.*; import java.util.*;
@Entity @Table(name="specifications")
public class Specification {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="specification_id") private Integer specificationId;
 @Column(name="specification_name",nullable=false,unique=true,length=100) private String specificationName;
 @Enumerated(EnumType.STRING) @Column(name="data_type",length=7) private SpecificationDataType dataType=SpecificationDataType.TEXT;
 @Column(length=30) private String unit; @Column(length=255) private String description;
 @OneToMany(mappedBy="specification") private List<PartSpecification> partSpecifications=new ArrayList<>();
 public Specification(){}
 public Integer getSpecificationId(){return specificationId;} public void setSpecificationId(Integer v){specificationId=v;}
 public String getSpecificationName(){return specificationName;} public void setSpecificationName(String v){specificationName=v;}
 public SpecificationDataType getDataType(){return dataType;} public void setDataType(SpecificationDataType v){dataType=v;}
 public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public List<PartSpecification> getPartSpecifications(){return partSpecifications;} public void setPartSpecifications(List<PartSpecification> v){partSpecifications=v;}
}
