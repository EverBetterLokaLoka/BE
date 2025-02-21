CREATE TABLE blacklisted_token
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    token       VARCHAR(255)          NOT NULL,
    expiry_time datetime              NOT NULL,
    CONSTRAINT pk_blacklistedtoken PRIMARY KEY (id)
);

ALTER TABLE blacklisted_token
    ADD CONSTRAINT uc_blacklistedtoken_token UNIQUE (token);