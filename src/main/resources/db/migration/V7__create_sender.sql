CREATE TABLE mss_sender
(
    id integer NOT NULL,
    shop_id integer,
    language_id integer,
    email character varying COLLATE pg_catalog."default",
    CONSTRAINT mss_sender_pkey PRIMARY KEY (id)
)