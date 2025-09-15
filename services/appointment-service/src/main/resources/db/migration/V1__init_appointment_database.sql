CREATE TABLE appointments
(
    id                         BIGINT AUTO_INCREMENT NOT NULL,
    created_by                 VARCHAR(255)          NOT NULL,
    last_modified_by           VARCHAR(255)          NULL,
    created_date               datetime              NOT NULL,
    last_modified_date         datetime              NULL,
    doctor_id                  BINARY(16)            NOT NULL,
    patient_id                 BINARY(16)            NOT NULL,
    appointment_date           datetime              NOT NULL,
    estimated_duration_minutes INT                   NULL,
    end_time                   datetime              NULL,
    reason                     VARCHAR(255)          NULL,
    notes                      VARCHAR(255)          NULL,
    status                     VARCHAR(255)          NOT NULL,
    cancelled_by               BINARY(16)            NULL,
    cancelled_date             datetime              NULL,
    CONSTRAINT pk_appointments PRIMARY KEY (id)
);