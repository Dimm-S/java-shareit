CREATE TABLE if NOT EXISTS users
(
    id
    serial
    NOT
    NULL
    CONSTRAINT
    pk_user
    PRIMARY
    KEY,
    NAME
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    512
) NOT NULL CONSTRAINT uq_user_email UNIQUE
    );

CREATE TABLE if NOT EXISTS requests
(
    id
    serial
    NOT
    NULL
    CONSTRAINT
    pk_request
    PRIMARY
    KEY,
    description
    VARCHAR
(
    512
) NOT NULL,
    requester_id bigint CONSTRAINT fk_requester_id REFERENCES users
(
    id
),
    created TIMESTAMP WITHOUT TIME ZONE
    );

CREATE TABLE if NOT EXISTS items
(
    id
    serial
    NOT
    NULL
    CONSTRAINT
    pk_item
    PRIMARY
    KEY,
    NAME
    VARCHAR
(
    100
) NOT NULL,
    description VARCHAR
(
    200
),
    is_available boolean NOT NULL,
    owner_id bigint NOT NULL CONSTRAINT owner_id_foreign_key REFERENCES users
(
    id
),
    request_id bigint CONSTRAINT fk_request_id REFERENCES requests
(
    id
)
    );

CREATE TABLE if NOT EXISTS comments
(
    id
    serial
    NOT
    NULL
    CONSTRAINT
    pk_comment
    PRIMARY
    KEY,
    text
    text,
    item_id
    bigint
    NOT
    NULL
    CONSTRAINT
    fk_item_id
    REFERENCES
    items
(
    id
),
    author_id bigint NOT NULL CONSTRAINT fk_author_id REFERENCES users
(
    id
),
    created TIMESTAMP WITHOUT TIME ZONE
    );

CREATE TABLE if NOT EXISTS bookings
(
    id
    serial
    NOT
    NULL
    CONSTRAINT
    pk_booking
    PRIMARY
    KEY,
    start_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    end_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE,
    item_id
    bigint
    NOT
    NULL
    CONSTRAINT
    fk_items_id
    REFERENCES
    items
(
    id
),
    booker_id bigint NOT NULL CONSTRAINT fk_booker_id REFERENCES users
(
    id
),
    status INT
    );

TRUNCATE users, requests, items, comments, bookings RESTART IDENTITY;