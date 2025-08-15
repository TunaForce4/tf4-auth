package com.tunaforce.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    protected Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    protected Instant updatedAt;

    @Column(name = "deleted_at")
    protected Instant deletedAt;

    @Column(name = "deleted_by")
    protected UUID deletedBy;

    // 삭제 메서드
    public void markDeleted(UUID userId) {
        this.deletedAt = Instant.now();
        this.deletedBy = userId;
    }

}
