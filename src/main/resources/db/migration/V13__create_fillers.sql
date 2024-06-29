CREATE TABLE mss_fillers
(
    filler_id integer NOT NULL DEFAULT nextval('filler_id_seq'::regclass),
    name character varying(50) COLLATE pg_catalog."default",
    shop_id integer,
    language_id integer,
    sql text COLLATE pg_catalog."default",
    cruser integer,
    crdate timestamp without time zone,
    upduser integer,
    upddate timestamp without time zone,
    mss_type_id integer,
    CONSTRAINT mss_fillers_mss_type_id_fkey FOREIGN KEY (mss_type_id, shop_id)
        REFERENCES mss_types (mss_type_id, shop_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT mss_fillers_shop_id_fkey FOREIGN KEY (shop_id)
        REFERENCES mss_shops (shop_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)