package com.phromec.management.model;

import com.phromec.management.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false, length = 50)
    private String action;
    @Column(name = "table_name", length = 100)
    private String tableName;
    @Column(name = "record_id")
    private Integer recordId;
    @Column(name = "old_value", columnDefinition = "json")
    private String oldValue;
    @Column(name = "new_value", columnDefinition = "json")
    private String newValue;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long v) {
        auditId = v;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User v) {
        user = v;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String v) {
        action = v;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String v) {
        tableName = v;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer v) {
        recordId = v;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String v) {
        oldValue = v;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String v) {
        newValue = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        createdAt = v;
    }
}
