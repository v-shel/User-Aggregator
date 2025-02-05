DROP TABLE IF EXISTS users;
CREATE TABLE users (
    user_id    uuid primary key not null default gen_random_uuid(),
    login      varchar,
    first_name varchar,
    last_name  varchar
);

INSERT INTO users (login, first_name, last_name) VALUES ('login1', 'John', 'Smith');
INSERT INTO users (login, first_name, last_name) VALUES ('login2', 'Ralph', 'Cheers');

CREATE USER user2 WITH PASSWORD 'pass2';
CREATE DATABASE test_db2 OWNER user2;