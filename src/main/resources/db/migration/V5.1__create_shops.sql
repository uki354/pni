CREATE TABLE mss_shops
(
    shop_id integer NOT NULL DEFAULT nextval('mss_shops_id_seq'::regclass),
    name character varying COLLATE pg_catalog."default",
    CONSTRAINT mss_shops_pkey PRIMARY KEY (shop_id)
)