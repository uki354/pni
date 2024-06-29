CREATE TABLE mss_templates
(
    mss_template_id integer NOT NULL DEFAULT nextval('mss_templates_id_seq'::regclass),
    language_id integer,
    shop_id integer,
    mss_type_id integer,
    name character varying(100) COLLATE pg_catalog."default",
    mail_subject character varying(70) COLLATE pg_catalog."default",
    mail_from character varying COLLATE pg_catalog."default",
    coorders_url character varying(150) COLLATE pg_catalog."default",
    html_template text COLLATE pg_catalog."default",
    json_template text COLLATE pg_catalog."default",
    online_version_url character varying(150) COLLATE pg_catalog."default",
    filler_id integer,
    state character varying COLLATE pg_catalog."default",
    cruser integer,
    crdate timestamp without time zone,
    upduser integer,
    upddate timestamp without time zone,
    CONSTRAINT mss_templates_pkey PRIMARY KEY (mss_template_id),
    CONSTRAINT fk_mss_type_id FOREIGN KEY (mss_type_id, shop_id)
        REFERENCES mss_types (mss_type_id, shop_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_shop_id FOREIGN KEY (shop_id)
        REFERENCES mss_shops (shop_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)