create sequence refresh_token_sequence start with 1 increment by 1;

create table refresh_tokens (
    id bigint not null primary key,
    token varchar(255),
    created timestamp not null,

    constraint unique_refresh_token unique(token)
);