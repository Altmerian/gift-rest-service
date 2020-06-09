CREATE TABLE certificates
(
    id integer NOT NULL DEFAULT nextval('certificates_id_seq'::regclass),
    name character varying(64) NOT NULL,
    description character varying(128),
    price numeric(14,2) NOT NULL,
    creation_date timestamp(0) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modification_date timestamp(0) with time zone,
    duration_in_days smallint NOT NULL DEFAULT 90,
    deleted boolean DEFAULT false,
    CONSTRAINT certificates_pkey PRIMARY KEY (id)
)

CREATE TABLE tags
(
    id integer NOT NULL DEFAULT nextval('tags_id_seq'::regclass),
    name character varying(64) NOT NULL,
    CONSTRAINT tags_pkey PRIMARY KEY (id),
    CONSTRAINT tag_name UNIQUE (name)
)

CREATE TABLE certificates_tags
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
)

