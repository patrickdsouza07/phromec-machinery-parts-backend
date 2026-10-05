package com.phromec.management.model;
import jakarta.persistence.*; import java.time.LocalDateTime; import java.util.*;
@Entity @Table(name="materials")
public class Material {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="material_id") private Integer materialId;
 @Column(name="material_code",nullable=false,unique=true,length=30) private String materialCode;
 @Column(name="material_name",nullable=false,unique=true,length=100) private String materialName;
 @Column(length=255) private String description; @Column(length=30) private String unit;
 @Enumerated(EnumType.STRING) @Column(length=8) private RecordStatus status=RecordStatus.ACTIVE;
 @Column(name="created_at") private LocalDateTime createdAt;
 @OneToMany(mappedBy="material") private List<PartVariantMaterial> variantMaterials=new ArrayList<>();
 public Material(){}
 public Integer getMaterialId(){return materialId;} public void setMaterialId(Integer v){materialId=v;}
 public String getMaterialCode(){return materialCode;} public void setMaterialCode(String v){materialCode=v;}
 public String getMaterialName(){return materialName;} public void setMaterialName(String v){materialName=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public String getUnit(){return unit;} public void setUnit(String v){unit=v;}
 public RecordStatus getStatus(){return status;} public void setStatus(RecordStatus v){status=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
 public List<PartVariantMaterial> getVariantMaterials(){return variantMaterials;} public void setVariantMaterials(List<PartVariantMaterial> v){variantMaterials=v;}
}
