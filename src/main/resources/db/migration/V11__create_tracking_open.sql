CREATE TABLE mss_tracking_open
(
    template_id integer NOT NULL,
    clients_id integer NOT NULL,
    count integer,
    user_agent character varying COLLATE pg_catalog."default",
    os character varying COLLATE pg_catalog."default",
    device character varying COLLATE pg_catalog."default",
    client_uuid character varying COLLATE pg_catalog."default" NOT NULL,
    open_at timestamp without time zone,
    last_open_at timestamp without time zone,
    CONSTRAINT "pk_clientUuuid_template_id" PRIMARY KEY (client_uuid, template_id)
)