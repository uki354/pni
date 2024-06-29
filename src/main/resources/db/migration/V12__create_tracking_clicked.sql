CREATE TABLE mss_tracking_clicked
(
    template_id integer NOT NULL,
    clients_id bigint NOT NULL,
    link character varying COLLATE pg_catalog."default" NOT NULL,
    count integer,
    client_uuid character varying COLLATE pg_catalog."default" NOT NULL,
    user_agent character varying COLLATE pg_catalog."default",
    os character varying COLLATE pg_catalog."default",
    device character varying COLLATE pg_catalog."default",
    open_at timestamp without time zone,
    last_open_at timestamp without time zone,
    CONSTRAINT pk_uuid_template_id_link PRIMARY KEY (template_id, link, client_uuid)
)