CREATE TABLE doctor_specialties
(
    doctor_id    BINARY(16) NOT NULL,
    specialty_id INT        NOT NULL,
    CONSTRAINT pk_doctor_specialties PRIMARY KEY (doctor_id, specialty_id)
);

CREATE TABLE doctors
(
    id              BINARY(16)   NOT NULL,
    first_name      VARCHAR(255) NOT NULL,
    last_name       VARCHAR(255) NOT NULL,
    specialty       VARCHAR(255) NOT NULL,
    medical_license VARCHAR(255) NOT NULL,
    phone           VARCHAR(15)  NULL,
    email           VARCHAR(255) NOT NULL,
    office_number   VARCHAR(255) NOT NULL,
    active          BIT(1)       NOT NULL,
    user_id         BINARY(16)   NOT NULL,
    CONSTRAINT pk_doctors PRIMARY KEY (id)
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

ALTER TABLE doctor_specialties
    ADD CONSTRAINT fk_docspe_on_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id);

ALTER TABLE doctor_specialties
    ADD CONSTRAINT fk_docspe_on_specialty FOREIGN KEY (specialty_id) REFERENCES specialties (id);