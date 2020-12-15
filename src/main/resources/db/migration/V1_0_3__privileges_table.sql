create sequence privileges_sequence start with 1000 increment by 1;

create table privileges (
    id bigint not null primary key,
    name varchar(255),

    constraint unique_privilege_name unique(name)
);