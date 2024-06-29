CREATE TABLE mss_types
(
    mss_type_id integer NOT NULL,
    shop_id integer NOT NULL,
    name character varying COLLATE pg_catalog."default",
    CONSTRAINT mss_types_pkey PRIMARY KEY (mss_type_id, shop_id),
    CONSTRAINT mss_types_shop_id_fkey FOREIGN KEY (shop_id)
        REFERENCES mss_shops (shop_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)