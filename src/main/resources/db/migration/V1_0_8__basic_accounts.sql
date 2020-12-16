-- Create basic privileges.
insert into privileges (id, name) values
    -- CRUD operations on ordinary users.
    (1, 'CREATE_USER'),
    (2, 'READ_USER'),
    (3, 'UPDATE_USER'),
    (4, 'DELETE_USER');

-- Create roles.
insert into roles (id, name) values
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_USER');

-- Assign privileges into roles.
insert into roles_privileges (role_id, privilege_id) values
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4);

-- Crete initial users.
insert into users (id, created, email, enabled, login, password) values
    -- Create user with login 'admin' and password 'admin'
    (1, now(), 'admin@localhost', true, 'admin', '$2a$10$ZXGDFbCfuYjfg2zysBi0rOeMbVoDLGOUamUbilxlmV.VaqnPMiAJy');

-- Assign roles to users
insert into users_roles (user_id, role_id) values
    (1, 1);
