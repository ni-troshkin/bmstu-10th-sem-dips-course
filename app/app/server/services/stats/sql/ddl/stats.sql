CREATE TABLE events
(
    event_id SERIAL PRIMARY KEY,
    event_uuid uuid NOT NULL,
    username VARCHAR(100),
    action VARCHAR(100) NOT NULL,
    event_start TIMESTAMP NOT NULL,
    event_end TIMESTAMP NOT NULL,
    params JSON,
    service VARCHAR(50) NOT NULL
);

create extension "uuid-ossp";
