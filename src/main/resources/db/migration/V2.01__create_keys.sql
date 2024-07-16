CREATE TABLE IF NOT EXISTS keys_table (

    id serial PRIMARY KEY,
    key varchar(20),
    user_id integer references users(id) not null,
    api varchar(20),
    is_active integer
)