package com.phromec.management.model;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.*;
@Entity @Table(name="pricing")
public class Pricing {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="pricing_id") private Integer pricingId;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="variant_id",nullable=false) private PartVariant variant;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal price; @Column(length=10) private String currency="INR";
 @Column(name="effective_from",nullable=false) private LocalDate effectiveFrom; @Column(name="effective_to") private LocalDate effectiveTo;
 @Enumerated(EnumType.STRING) @Column(name="price_type",length=8) private PriceType priceType=PriceType.SELLING;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="created_by") private User createdBy;
 @Column(name="created_at") private LocalDateTime createdAt;
 public Pricing(){}
 public Integer getPricingId(){return pricingId;} public void setPricingId(Integer v){pricingId=v;}
 public PartVariant getVariant(){return variant;} public void setVariant(PartVariant v){variant=v;}
 public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
 public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;}
 public LocalDate getEffectiveFrom(){return effectiveFrom;} public void setEffectiveFrom(LocalDate v){effectiveFrom=v;}
 public LocalDate getEffectiveTo(){return effectiveTo;} public void setEffectiveTo(LocalDate v){effectiveTo=v;}
 public PriceType getPriceType(){return priceType;} public void setPriceType(PriceType v){priceType=v;}
 public User getCreatedBy(){return createdBy;} public void setCreatedBy(User v){createdBy=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
