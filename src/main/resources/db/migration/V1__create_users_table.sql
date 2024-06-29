CREATE TABLE users
(
    users_id integer NOT NULL DEFAULT 0,
    first_name character varying(30) COLLATE pg_catalog."default",
    last_name character varying(30) COLLATE pg_catalog."default",
    email character varying(50) COLLATE pg_catalog."default",
    crypted_password character varying COLLATE pg_catalog."default",
    status integer,
    create_date timestamp without time zone,
    last_login_date timestamp without time zone,
    last_login_ip_address character varying(30) COLLATE pg_catalog."default",
    last_token_value character varying COLLATE pg_catalog."default",
    last_token_expire_date timestamp without time zone,
    CONSTRAINT users_pkey PRIMARY KEY (users_id)
)