package fr.ndev.insurance.model;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AuditableEntityListener {

    @PrePersist
    public void setCreatedAtAndUpdatedAt(Object entity) {
        if (entity instanceof Auditable) {
            LocalDateTime now = LocalDateTime.now();
            Auditable auditable = (Auditable) entity;
            auditable.setCreatedAt(now);
            auditable.setUpdatedAt(now);
        }
    }

    @PreUpdate
    public void setUpdatedAt(Object entity) {
        if (entity instanceof Auditable) {
            Auditable auditable = (Auditable) entity;
            auditable.setUpdatedAt(LocalDateTime.now());
        }
    }
}