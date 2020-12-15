create table verification_tokens (
    id bigint not null primary key,
    expire timestamp not null,
    token varchar(255),
    user_id bigint not null,

    constraint unique_verification_token unique(token),
    constraint verification_token_user_fk foreign key(user_id) references users
);