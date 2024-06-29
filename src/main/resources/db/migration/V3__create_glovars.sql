CREATE TABLE glovars
(
    glo_id character varying COLLATE pg_catalog."default" NOT NULL,
    glo_value character varying COLLATE pg_catalog."default",
    cruser integer,
    crdate timestamp without time zone,
    upduser integer,
    upddate timestamp without time zone,
    CONSTRAINT glovars_pkey PRIMARY KEY (glo_id)
)