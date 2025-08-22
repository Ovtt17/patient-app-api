CREATE TABLE permissions
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)       NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE role_permissions
(
    permission_id INT NOT NULL,
    role_id       INT NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (permission_id, role_id)
);

CREATE TABLE roles
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)       NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE tokens
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    token        VARCHAR(255)          NULL,
    created_at   datetime              NULL,
    expires_at   datetime              NULL,
    validated_at datetime              NULL,
    user_id      BINARY(16)            NULL,
    CONSTRAINT pk_tokens PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                 BINARY(16)   NOT NULL,
    email              VARCHAR(255) NOT NULL,
    username           VARCHAR(255) NOT NULL,
    password           VARCHAR(255) NULL,
    provider           VARCHAR(255) NOT NULL,
    phone              VARCHAR(15)  NULL,
    profile_picture    TEXT         NULL,
    account_locked     BIT(1)       NOT NULL,
    enabled            BIT(1)       NOT NULL,
    created_date       datetime     NOT NULL,
    last_modified_date datetime     NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    roles_id INT        NOT NULL,
    users_id BINARY(16) NOT NULL
);

ALTER TABLE permissions
    ADD CONSTRAINT uc_permissions_name UNIQUE (name);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE tokens
    ADD CONSTRAINT FK_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_id) REFERENCES roles (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (users_id) REFERENCES users (id);