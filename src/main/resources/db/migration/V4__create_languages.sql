CREATE TABLE languages
(
    language_id integer NOT NULL,
    name character varying COLLATE pg_catalog."default",
    CONSTRAINT languages_pkey PRIMARY KEY (language_id)
)