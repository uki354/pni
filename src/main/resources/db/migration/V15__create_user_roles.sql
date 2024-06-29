CREATE TABLE users_roles
(
    users_id integer NOT NULL,
    roles_id integer NOT NULL,
    CONSTRAINT roles_id FOREIGN KEY (roles_id)
        REFERENCES roles (roles_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT users_id FOREIGN KEY (users_id)
        REFERENCES users (users_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
