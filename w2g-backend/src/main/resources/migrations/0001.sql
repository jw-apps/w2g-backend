--liquibase formatted sql

--changeset jwirth:1
CREATE TABLE IF NOT EXISTS videos
(
    ID                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    filename            VARCHAR(100)         NOT NULL,
    hash                BINARY(16)          NOT NULL
) ENGINE=MyISAM;
--rollback drop table videos;