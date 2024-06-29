CREATE TABLE clients
(
    clients_id bigint NOT NULL,
    gender character varying COLLATE pg_catalog."default",
    first_name character varying(30) COLLATE pg_catalog."default",
    last_name character varying(50) COLLATE pg_catalog."default",
    addition_1 character varying(50) COLLATE pg_catalog."default",
    addition_2 character varying(50) COLLATE pg_catalog."default",
    street character varying(50) COLLATE pg_catalog."default",
    zip_code integer,
    city character varying(50) COLLATE pg_catalog."default",
    country_code character varying(10) COLLATE pg_catalog."default",
    email character varying(80) COLLATE pg_catalog."default",
    mediacode integer,
    crdate date,
    upddate date,
    CONSTRAINT clients_pkey PRIMARY KEY (clients_id)
);



CREATE TABLE clients_additional_info
(
    clients_id bigint NOT NULL,
    language character varying(3) COLLATE pg_catalog."default",
    date_of_birth date,
    phone character varying(25) COLLATE pg_catalog."default",
    mobile character varying(25) COLLATE pg_catalog."default",
    free_flag character varying(12) COLLATE pg_catalog."default",
    bearbg character varying(1) COLLATE pg_catalog."default",
    dubious character varying(1) COLLATE pg_catalog."default",
    email_for_invoice character varying(80) COLLATE pg_catalog."default",
    CONSTRAINT clients_additional_info_pkey PRIMARY KEY (clients_id)
);


INSERT INTO clients (clients_id,gender,first_name,last_name,email,zip_code,mediacode) VALUES (1,'m','uros','stevanovic','uros.3.stevanovic@gmail.com',0,0);



