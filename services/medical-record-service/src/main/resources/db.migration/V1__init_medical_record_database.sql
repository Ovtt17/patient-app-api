CREATE TABLE medical_records
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_by         VARCHAR(255)          NOT NULL,
    last_modified_by   VARCHAR(255)          NULL,
    created_date       datetime              NOT NULL,
    last_modified_date datetime              NULL,
    patient_id         BINARY(16)            NULL,
    appointment_id     BIGINT                NULL,
    weight             DOUBLE                NULL,
    height             DOUBLE                NULL,
    blood_type         VARCHAR(255)          NULL,
    allergies          VARCHAR(255)          NULL,
    chronic_diseases   VARCHAR(255)          NULL,
    medications        VARCHAR(255)          NULL,
    diagnostic         VARCHAR(255)          NULL,
    CONSTRAINT pk_medical_records PRIMARY KEY (id)
);