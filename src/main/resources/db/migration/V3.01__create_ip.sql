CREATE TABLE IF NOT EXISTS ips(
    id serial PRIMARY KEY,
    user_id integer references users(id) not null,
    ip varchar(15)
)