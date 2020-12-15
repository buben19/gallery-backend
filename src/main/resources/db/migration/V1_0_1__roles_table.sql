create sequence roles_sequence start with 1 increment by 1;

create table roles (
    id bigint not null primary key,
    name varchar(255),

    constraint unique_role_name unique(name)
);
