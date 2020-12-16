create sequence refresh_token_sequence start with 1 increment by 1;

create table refresh_tokens (
    id bigint not null primary key,
    token varchar(255) not null,
    created timestamp not null,
    expire timestamp,
    user_id bigint not null,

    constraint unique_refresh_token unique(token),
    constraint refresh_token_user_fk foreign key(user_id) references users
);