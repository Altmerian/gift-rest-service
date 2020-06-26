-- Database: giftServiceTest

-- DROP DATABASE "giftServiceTest";

-- CREATE DATABASE "giftServiceTest"
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     CONNECTION LIMIT = -1;
--
-- COMMENT ON DATABASE "giftServiceTest"
--     IS 'EPAM FARM task 2 REST service integration test db';

DROP TABLE IF EXISTS certificates CASCADE ;
DROP TABLE IF EXISTS certificates_tags;
DROP TABLE IF EXISTS tags;

CREATE TABLE IF NOT EXISTS certificates
(
    id serial NOT NULL,
    name character varying(64) NOT NULL,
    description character varying(128),
    price numeric(14,2) NOT NULL,
    creation_date timestamp(0) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modification_date timestamp(0) with time zone,
    duration_in_days smallint NOT NULL DEFAULT 90,
    CONSTRAINT certificates_pkey PRIMARY KEY (id),
    CONSTRAINT positive_price CHECK (price > 0::numeric),
    CONSTRAINT positive_duration CHECK (duration_in_days > 0)
);

ALTER SEQUENCE certificates_id_seq
    RESTART 3;

CREATE TABLE IF NOT EXISTS tags
(
    id serial NOT NULL,
    name character varying(64) NOT NULL,
    CONSTRAINT tags_pkey PRIMARY KEY (id),
    CONSTRAINT tag_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS certificates_tags
(
    certificate_id integer NOT NULL,
    tag_id integer NOT NULL,
    CONSTRAINT certificate_id_fk FOREIGN KEY (certificate_id)
        REFERENCES certificates (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT tag_id_fk FOREIGN KEY (tag_id)
        REFERENCES tags (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

