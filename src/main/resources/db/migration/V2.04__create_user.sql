alter table users
add column code VARCHAR(3),
add column verified int default 0;