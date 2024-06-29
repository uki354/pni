CREATE TABLE threads_monitoring
(
    thread_id character varying COLLATE pg_catalog."default" NOT NULL,
    template_id integer,
    scheduled_date_time timestamp without time zone,
    repeat_after integer,
    state character varying COLLATE pg_catalog."default",
    CONSTRAINT threads_pkey PRIMARY KEY (thread_id)
)