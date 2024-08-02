CREATE TABLE IF NOT EXISTS events(
    id serial PRIMARY KEY,
    user_id integer references users(id) not null,
    city varchar(100),
    country varchar(60)
)