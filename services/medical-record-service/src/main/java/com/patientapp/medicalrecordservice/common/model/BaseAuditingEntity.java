package com.patientapp.medicalrecordservice.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseAuditingEntity {

    @CreatedBy
    @JoinColumn(name = "created_by", nullable = false, updatable = false)
    private String createdBy; // User ID of the creator

    @LastModifiedBy
    @JoinColumn(name = "last_modified_by", insertable = false)
    private String lastModifiedBy; // User ID of the last modifier

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", insertable = false)
    private Instant lastModifiedDate;
}

