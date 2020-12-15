create sequence images_sequence start with 1 increment by 1;

create table images (
    id bigint not null primary key,
    path varchar(255),
    title varchar(255),
    owner_id bigint not null,

    constraint image_user_fk foreign key (owner_id) references users
);
