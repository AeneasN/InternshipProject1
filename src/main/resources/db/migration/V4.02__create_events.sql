alter table events
    add column events text[],
    add column tag boolean,
    drop column user_id,
    drop column city,
    drop column country