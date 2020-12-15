create table roles_privileges (
    role_id bigint not null,
    privilege_id bigint not null,

    constraint roles_privileges_role_fk foreign key (role_id) references roles,
    constraint roles_privileges_privilege_fk foreign key (privilege_id) references privileges
);