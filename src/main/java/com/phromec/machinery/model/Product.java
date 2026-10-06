package com.phromec.machinery.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long productId;

	@Column(name = "product_code", length = 50, nullable = false, unique = true)
	private String productCode;

	@Column(name = "product_name", length = 150, nullable = false)
	private String productName;

	@Column(name = "category", length = 100, nullable = false)
	private String category;

	@Column(name = "laser_type", length = 50)
	private String laserType;

	@Column(name = "laser_power_kw", precision = 6, scale = 2)
	private BigDecimal laserPowerKw;

	@Column(name = "cutting_area_mm", length = 30)
	private String cuttingAreaMm;

	@Column(name = "max_cutting_speed_mm_min")
	private Integer maxCuttingSpeedMmMin;

	@Column(name = "positioning_accuracy_mm", precision = 6, scale = 3)
	private BigDecimal positioningAccuracyMm;

	@Column(name = "repeatability_mm", precision = 6, scale = 3)
	private BigDecimal repeatabilityMm;

	@Column(name = "machine_weight_kg", precision = 10, scale = 2)
	private BigDecimal machineWeightKg;

	@Column(name = "machine_dimensions_mm", length = 50)
	private String machineDimensionsMm;

	@Column(name = "power_supply", length = 50)
	private String powerSupply;

	@Column(name = "price_inr", precision = 15, scale = 2)
	private BigDecimal priceInr;

	@Column(name = "stock_quantity")
	private Integer stockQuantity = 0;

	@Column(name = "reorder_level")
	private Integer reorderLevel = 0;

	@Column(name = "supplier_name", length = 150)
	private String supplierName;

	@Column(name = "warranty_months")
	private Integer warrantyMonths = 12;

	@Column(
			name = "status",
			columnDefinition = "ENUM('ACTIVE','INACTIVE','TERMINATED') DEFAULT 'ACTIVE'"
	)
	private String status = "ACTIVE";

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// Default Constructor
	public Product() {
	}

	// Getters and Setters

    public void setProductId(Long productId) {
		this.productId = productId;
	}

    public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

    public void setProductName(String productName) {
		this.productName = productName;
	}

    public void setCategory(String category) {
		this.category = category;
	}

    public void setLaserType(String laserType) {
		this.laserType = laserType;
	}

    public void setLaserPowerKw(BigDecimal laserPowerKw) {
		this.laserPowerKw = laserPowerKw;
	}

    public void setCuttingAreaMm(String cuttingAreaMm) {
		this.cuttingAreaMm = cuttingAreaMm;
	}

    public void setMaxCuttingSpeedMmMin(Integer maxCuttingSpeedMmMin) {
		this.maxCuttingSpeedMmMin = maxCuttingSpeedMmMin;
	}

    public void setPositioningAccuracyMm(BigDecimal positioningAccuracyMm) {
		this.positioningAccuracyMm = positioningAccuracyMm;
	}

    public void setRepeatabilityMm(BigDecimal repeatabilityMm) {
		this.repeatabilityMm = repeatabilityMm;
	}

    public void setMachineWeightKg(BigDecimal machineWeightKg) {
		this.machineWeightKg = machineWeightKg;
	}

    public void setMachineDimensionsMm(String machineDimensionsMm) {
		this.machineDimensionsMm = machineDimensionsMm;
	}

    public void setPowerSupply(String powerSupply) {
		this.powerSupply = powerSupply;
	}

    public void setPriceInr(BigDecimal priceInr) {
		this.priceInr = priceInr;
	}

    public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

    public void setReorderLevel(Integer reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

    public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

    public void setWarrantyMonths(Integer warrantyMonths) {
		this.warrantyMonths = warrantyMonths;
	}

    public void setStatus(String status) {
		this.status = status;
	}

    public void setDescription(String description) {
		this.description = description;
	}

    public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
