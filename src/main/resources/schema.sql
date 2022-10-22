CREATE TABLE IF NOT EXISTS users
(
    id    serial NOT NULL  CONSTRAINT pk_user PRIMARY KEY,
    name  varchar(255) NOT NULL,
    email varchar(512) NOT NULL CONSTRAINT uq_user_email UNIQUE
    );

CREATE TABLE  IF NOT EXISTS requests
(
    id serial NOT NULL CONSTRAINT pk_request PRIMARY KEY,
    description varchar(512) NOT NULL,
    requestor_id bigint CONSTRAINT fk_requestor_id REFERENCES users (id)
);

create table if not exists items
(
    id serial NOT NULL CONSTRAINT pk_item primary key,
    name         varchar(100) not null,
    description  varchar(200),
    is_available boolean      not null,
    owner_id     bigint       not null    constraint owner_id_foreign_key   references users (id),
    request_id   bigint       CONSTRAINT fk_request_id REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id serial NOT NULL CONSTRAINT pk_comment PRIMARY KEY,
    text text,
    item_id bigint NOT NULL CONSTRAINT fk_item_id REFERENCES items (id),
    author_id bigint NOT NULL CONSTRAINT fk_author_id REFERENCES users (id),
    created timestamp without time zone
);

CREATE TABLE IF NOT EXISTS bookings
(
    id serial NOT NULL CONSTRAINT pk_booking PRIMARY KEY,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    item_id bigint NOT NULL CONSTRAINT fk_items_id REFERENCES items (id),
    booker_id bigint NOT NULL CONSTRAINT fk_booker_id REFERENCES users (id),
    status int
);
