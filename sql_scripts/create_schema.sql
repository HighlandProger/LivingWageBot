CREATE ROLE bot;
ALTER USER bot WITH PASSWORD '33';
ALTER ROLE bot WITH LOGIN;

CREATE DATABASE region_living_wage_db WITH OWNER bot;
====================================================
psql region_living_wage_db;
====================================================

CREATE SCHEMA IF NOT EXISTS ncs_bot;
SET
SEARCH_PATH = ncs_bot;

DROP TABLE IF EXISTS ncs_bot.region_living_wage CASCADE;
CREATE TABLE ncs_bot.region_living_wage
(
    id                     bigserial primary key,
    region_name            varchar NOT NULL,
    human_living_wage      integer NOT NULL,
    employee_living_wage   integer NOT NULL,
    retiree_living_wage    integer NOT NULL,
    child_living_wage      integer NOT NULL
);

DROP TABLE IF EXISTS ncs_bot.chats CASCADE;
CREATE TABLE ncs_bot.chats
(
    id                     bigserial primary key,
    username               varchar,
    status                 varchar NOT NULL,
    employee_count         integer,
    child_count            integer,
    retiree_count          integer,
    region_living_wage_id  bigint,
    is_admin               boolean
);

DROP TABLE IF EXISTS ncs_bot.telegram_data CASCADE;
CREATE TABLE ncs_bot.telegram_data
(
    id                     bigserial primary key,
    name                   varchar,
    text_message           text,
    photo_id               text,
    video_id               text,
    sticker_id             text
);

GRANT ALL PRIVILEGES ON SCHEMA ncs_bot TO bot;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA ncs_bot TO bot;
