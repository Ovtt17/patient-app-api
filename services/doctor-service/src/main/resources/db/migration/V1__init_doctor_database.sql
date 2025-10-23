CREATE TABLE doctor_specialties
(
    doctor_id    BINARY(16) NOT NULL,
    specialty_id INT        NOT NULL,
    CONSTRAINT pk_doctor_specialties PRIMARY KEY (doctor_id, specialty_id)
);

CREATE TABLE doctor_unavailabilities
(
    id                 INT AUTO_INCREMENT NOT NULL,
    created_by         VARCHAR(255)       NOT NULL,
    last_modified_by   VARCHAR(255)       NULL,
    created_date       datetime           NOT NULL,
    last_modified_date datetime           NULL,
    start_time         datetime           NOT NULL,
    end_time           datetime           NOT NULL,
    available          BIT(1)             NOT NULL,
    doctor_id          BINARY(16)         NOT NULL,
    CONSTRAINT pk_doctor_unavailabilities PRIMARY KEY (id)
);

CREATE TABLE doctors
(
    id                   BINARY(16)   NOT NULL,
    created_by           VARCHAR(255) NOT NULL,
    last_modified_by     VARCHAR(255) NULL,
    created_date         datetime     NOT NULL,
    last_modified_date   datetime     NULL,
    first_name           VARCHAR(255) NOT NULL,
    last_name            VARCHAR(255) NULL,
    email                VARCHAR(255) NOT NULL,
    gender               VARCHAR(255) NULL,
    phone                VARCHAR(15)  NULL,
    profile_picture      TEXT         NULL,
    medical_license      VARCHAR(255) NULL,
    office_number        VARCHAR(255) NULL,
    appointment_duration INT          NOT NULL,
    active               BIT(1)       NOT NULL,
    user_id              BINARY(16)   NOT NULL,
    zone_id              VARCHAR(255) NOT NULL,
    CONSTRAINT pk_doctors PRIMARY KEY (id)
);

CREATE TABLE schedules
(
    id                 INT AUTO_INCREMENT NOT NULL,
    created_by         VARCHAR(255)       NOT NULL,
    last_modified_by   VARCHAR(255)       NULL,
    created_date       datetime           NOT NULL,
    last_modified_date datetime           NULL,
    day_of_week        VARCHAR(255)       NOT NULL,
    start_time         time               NOT NULL,
    end_time           time               NOT NULL,
    doctor_id          BINARY(16)         NOT NULL,
    CONSTRAINT pk_schedules PRIMARY KEY (id)
);

CREATE TABLE specialties
(
    id            INT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)       NOT NULL,
    `description` VARCHAR(250)       NULL,
    CONSTRAINT pk_specialties PRIMARY KEY (id)
);

ALTER TABLE doctors
    ADD CONSTRAINT uc_doctors_email UNIQUE (email);

ALTER TABLE doctors
    ADD CONSTRAINT uc_doctors_medicallicense UNIQUE (medical_license);

ALTER TABLE doctors
    ADD CONSTRAINT uc_doctors_userid UNIQUE (user_id);

ALTER TABLE specialties
    ADD CONSTRAINT uc_specialties_name UNIQUE (name);

ALTER TABLE doctor_unavailabilities
    ADD CONSTRAINT FK_DOCTOR_UNAVAILABILITIES_ON_DOCTOR FOREIGN KEY (doctor_id) REFERENCES doctors (id);

ALTER TABLE schedules
    ADD CONSTRAINT FK_SCHEDULES_ON_DOCTOR FOREIGN KEY (doctor_id) REFERENCES doctors (id);

ALTER TABLE doctor_specialties
    ADD CONSTRAINT fk_docspe_on_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id);

ALTER TABLE doctor_specialties
    ADD CONSTRAINT fk_docspe_on_specialty FOREIGN KEY (specialty_id) REFERENCES specialties (id);