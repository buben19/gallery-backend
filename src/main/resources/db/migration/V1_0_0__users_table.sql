create sequence users_sequence start with 1 increment by 1;

create table users (
    id bigint not null primary key,
    created timestamp not null,
    email varchar(255),
    enabled boolean not null,
    first_name varchar(255),
    last_name varchar(255),
    login varchar(255),
    password varchar(255),

    constraint unique_user_login unique(login)
);