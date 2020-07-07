-- Database: giftService

-- DROP DATABASE "giftService";

-- CREATE DATABASE "giftService"
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     CONNECTION LIMIT = -1;
--
-- COMMENT ON DATABASE "giftService"
--     IS 'EPAM FARM task 2 REST service db';

-- DROP TABLE certificates CASCADE ;
-- DROP TABLE certificates_tags;
-- DROP TABLE tags;

CREATE TABLE certificates
(
    id                serial                NOT NULL,
    name              character varying(64) NOT NULL,
    description       character varying(128),
    price             numeric(14, 2)        NOT NULL,
    creation_date     timestamp(0) with time zone    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modification_date timestamp(0) with time zone,
    duration_in_days  smallint              NOT NULL DEFAULT 90,
    CONSTRAINT certificates_pkey PRIMARY KEY (id),
    CONSTRAINT positive_price CHECK (price > 0::numeric),
    CONSTRAINT positive_duration CHECK (duration_in_days > 0)
);

CREATE TABLE tags
(
    id   serial                NOT NULL,
    name character varying(64) NOT NULL,
    CONSTRAINT tags_pkey PRIMARY KEY (id),
    CONSTRAINT tag_name UNIQUE (name)
);

CREATE TABLE certificates_tags
(
    certificate_id integer NOT NULL,
    tag_id         integer NOT NULL,
    CONSTRAINT certificate_id_fk FOREIGN KEY (certificate_id)
        REFERENCES certificates (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT tag_id_fk FOREIGN KEY (tag_id)
        REFERENCES tags (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE TYPE user_role AS ENUM
    ('GUEST', 'USER', 'ADMIN');

CREATE TABLE users
(
    id         serial                NOT NULL,
    email      character varying(64) NOT NULL,
    password   character varying(32) NOT NULL,
    first_name character varying(32),
    last_name  character varying(32),
    user_role  user_role             NOT NULL DEFAULT 'USER'::user_role,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email UNIQUE (email)
);

CREATE TABLE orders
(
    id            serial  NOT NULL,
    cost          numeric(14, 2),
    creation_date timestamp(0) with time zone DEFAULT CURRENT_TIMESTAMP,
    user_id       integer NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

CREATE TABLE orders_certificates
(
    order_id       integer NOT NULL,
    certificate_id integer NOT NULL,
    CONSTRAINT certificate_id_fk FOREIGN KEY (certificate_id)
        REFERENCES certificates (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT order_id_fk FOREIGN KEY (order_id)
        REFERENCES orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);





