CREATE TABLE mss_to_send
(
    clients_id bigint NOT NULL,
    email character varying COLLATE pg_catalog."default",
    first_name character varying COLLATE pg_catalog."default",
    last_name character varying COLLATE pg_catalog."default",
    gender character varying COLLATE pg_catalog."default",
    date_of_birth date,
    state integer,
    send_time timestamp without time zone,
    template_id integer NOT NULL,
    client_uuid character varying COLLATE pg_catalog."default",
    id bigint NOT NULL DEFAULT nextval('mss_to_send_id_seq'::regclass),
    CONSTRAINT pk_id PRIMARY KEY (id),
    CONSTRAINT mss_to_send_uq_email_and_template_id UNIQUE (email, template_id)
)
