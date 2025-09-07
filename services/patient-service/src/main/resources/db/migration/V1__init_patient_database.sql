CREATE TABLE patients
(
    id                 BINARY(16)    NOT NULL,
    active             BIT(1)        NOT NULL,
    user_id            BINARY(16)    NOT NULL,
    created_date       datetime      NOT NULL,
    last_modified_date datetime      NULL,
    weight             DOUBLE        NULL,
    height             DOUBLE        NULL,
    birth_date         date          NULL,
    notes              VARCHAR(1000) NULL,
    CONSTRAINT pk_patients PRIMARY KEY (id)
);

ALTER TABLE patients
    ADD CONSTRAINT uc_patients_userid UNIQUE (user_id);