package com.phromec.management.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "permission_name", nullable = false, unique = true, length = 100)
    private String permissionName;

    public Permission() {}
    public Permission(String permissionName) { this.permissionName = permissionName; }

}
