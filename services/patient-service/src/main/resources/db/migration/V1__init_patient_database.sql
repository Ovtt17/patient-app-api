CREATE TABLE patients
(
    id                 BINARY(16)   NOT NULL,
    first_name         VARCHAR(255) NOT NULL,
    last_name          VARCHAR(255) NULL,
    email              VARCHAR(255) NOT NULL,
    gender             VARCHAR(255) NULL,
    phone              VARCHAR(15)  NULL,
    profile_picture    TEXT         NULL,
    active             BIT(1)       NOT NULL,
    user_id            BINARY(16)   NOT NULL,
    created_date       datetime     NOT NULL,
    last_modified_date datetime     NULL,
    weight             DOUBLE       NULL,
    height             DOUBLE       NULL,
    birth_date         date         NULL,
    notes              VARCHAR(500) NULL,
    CONSTRAINT pk_patients PRIMARY KEY (id)
);

ALTER TABLE patients
    ADD CONSTRAINT uc_patients_email UNIQUE (email);

ALTER TABLE patients
    ADD CONSTRAINT uc_patients_userid UNIQUE (user_id);