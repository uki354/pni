CREATE TABLE roles
(
    roles_id integer NOT NULL DEFAULT nextval('roles_roles_id_seq'::regclass),
    name character varying COLLATE pg_catalog."default",
    CONSTRAINT roles_pkey PRIMARY KEY (roles_id)
)
