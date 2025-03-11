package fr.ndev.insurance.model;

import java.time.LocalDateTime;

public interface Auditable {
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
}