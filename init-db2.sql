DROP TABLE IF EXISTS user_table;
CREATE TABLE user_table (
    ldap_login varchar primary key not null,
    name       varchar,
    surname    varchar
);

INSERT INTO user_table (ldap_login, name, surname) VALUES ('ldap_login1', 'Jain', 'Melt');
INSERT INTO user_table (ldap_login, name, surname) VALUES ('ldap_login2', 'Keith', 'Brooks');
