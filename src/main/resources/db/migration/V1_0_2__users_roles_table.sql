create table users_roles (
    user_id bigint not null,
    role_id bigint not null,

    constraint users_roles_user_fk foreign key (user_id) references users,
    constraint users_roles_role_fk foreign key (role_id) references roles
);