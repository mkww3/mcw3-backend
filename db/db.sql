CREATE DATABASE porto;
-- Remember, do not store stuff like this in repo :D
CREATE USER spring WITH PASSWORD 'porto';
GRANT ALL PRIVILEGES ON DATABASE porto to spring;
\connect porto
CREATE SCHEMA porto;

CREATE TABLE porto.accounts
(
    id             UUID PRIMARY KEY,
    vault_id       VARCHAR UNIQUE,
    iban           VARCHAR,
    public_key     VARCHAR,
    wallet_address VARCHAR
);

CREATE TABLE porto.sessions
(
    id         UUID PRIMARY KEY,
    account_id UUID,
    token      UUID,
    expiry     TIMESTAMPTZ
);

CREATE TABLE porto.projects
(
    id                     UUID PRIMARY KEY,
    name                   VARCHAR,
    logo                   VARCHAR,
    description            VARCHAR,
    status                 VARCHAR,
    website_url            VARCHAR,
    twitter_url            VARCHAR,
    telegram_url           VARCHAR,
    medium_url             VARCHAR,
    token_contract_address VARCHAR
);

CREATE TABLE porto.tokens
(
    id            UUID PRIMARY KEY,
    project_id    UUID REFERENCES porto.projects (id),
    name          VARCHAR,
    token_address VARCHAR,
    symbol        VARCHAR,
    chain         VARCHAR,
    logo          VARCHAR
);

CREATE TABLE porto.transactions
(
    id           UUID PRIMARY KEY,
    price        DOUBLE PRECISION,
    date         TIMESTAMPTZ,
    token_amount DOUBLE PRECISION,
    token_id     UUID,
    account_id   UUID,
    symbol       VARCHAR,
    logo         VARCHAR,
    project_name VARCHAR,
    token_name   VARCHAR
);

INSERT INTO porto.projects (id, name, logo, description, status, website_url, twitter_url, telegram_url, medium_url, token_contract_address) VALUES ('57363b3e-ecc4-40b6-8b29-6ce8881a7657', 'Toucan', 'https://app.toucan.earth/logos/toucan/icon-light.svg', 'Scaling the impact of planet-positive projects by turning carbon credits into tokens.', 'VERIFIED', 'https://toucan.earth', 'https://twitter.com/ToucanProtocol', '', 'https://medium.com/@ToucanProtocol', '0x2f800db0fdb5223b3c3f354886d907a671414a7f');
INSERT INTO porto.projects (id, name, logo, description, status, website_url, twitter_url, telegram_url, medium_url, token_contract_address) VALUES ('97b724fc-3c0a-4fc8-95a0-8996ba03359e', 'VitaDAO', 'https://uploads-ssl.webflow.com/600ff0f8154936050d98ec01/6410305f2e949aeacdb08be1_VitaDAO%20Logo%20Darkmode.svg', 'Collectively funding and advancing longevity research in an open and democratic manner.', 'NOT_VERIFIED', 'https://www.vitadao.com/', 'https://twitter.com/vlta_dao', '', 'https://vitadao.medium.com/', '0x81f8f0bb1cb2a06649e51913a151f0e7ef6fa321');
INSERT INTO porto.projects (id, name, logo, description, status, website_url, twitter_url, telegram_url, medium_url, token_contract_address) VALUES ('f93981bf-5016-4edd-bacd-2990f3b92d26', 'VitaDAO', 'https://uploads-ssl.webflow.com/600ff0f8154936050d98ec01/6410305f2e949aeacdb08be1_VitaDAO%20Logo%20Darkmode.svg', 'Collectively funding and advancing longevity research in an open and democratic manner.', 'NOT_VERIFIED', 'https://www.vitadao.com/', 'https://twitter.com/vlta_dao', '', 'https://vitadao.medium.com/', '0x81f8f0bb1cb2a06649e51913a151f0e7ef6fa321');

INSERT INTO porto.tokens (id, project_id, name, token_address, symbol, chain, logo) VALUES ('3387bd40-5554-4c43-af36-d16ebf308b73', '57363b3e-ecc4-40b6-8b29-6ce8881a7657', 'Base Carbon Tonne', '0x83B844180f66Bbc3BE2E97C6179035AF91c4Cce8', 'BCT', 'MATIC', 'https://assets.coingecko.com/coins/images/19240/small/bct_exchange_enhanced.png?1641441498');
INSERT INTO porto.tokens (id, project_id, name, token_address, symbol, chain, logo) VALUES ('d8f6633a-5f01-49fe-a47a-daffb249f5e5', '97b724fc-3c0a-4fc8-95a0-8996ba03359e', 'VitaDAO', '0x81f8f0bb1cb2a06649e51913a151f0e7ef6fa321', 'VITA', 'ETH', 'https://raw.githubusercontent.com/Uniswap/assets/master/blockchains/ethereum/assets/0x81f8f0bb1cB2A06649E51913A151F0E7Ef6FA321/logo.png');
INSERT INTO porto.tokens (id, project_id, name, token_address, symbol, chain, logo) VALUES ('42d8c951-a600-4f2d-9b89-787e29e6abcc', 'f93981bf-5016-4edd-bacd-2990f3b92d26', 'VitaDAO', '0x81f8f0bb1cb2a06649e51913a151f0e7ef6fa321', 'VITA', 'ETH', 'https://raw.githubusercontent.com/Uniswap/assets/master/blockchains/ethereum/assets/0x81f8f0bb1cB2A06649E51913A151F0E7Ef6FA321/logo.png');

INSERT INTO porto.accounts (id, vault_id, iban, public_key, wallet_address) VALUES ('7da6e1fd-b935-493d-a5c4-8a04c4ae375d', '0x160f4d0a3df2bdb315f5d53719deb914b38e66f08d84334407f67111978a8bff', 'IS13 2635 6907 1360 2643 7306 84', 'pk-april-arizo-2g5uqmhhsf8r1aa8', '0x5f3849D4EDdFB13C7c4FfaaE9d023600aF9942Bb');

INSERT INTO porto.transactions (id, price, date, token_amount, token_id, account_id, symbol, logo, project_name, token_name) VALUES ('247ccc01-890c-4f5c-8033-12e05f42df83', 24.2, '2023-03-17 19:35:37.220881 +00:00', 49.23, '3387bd40-5554-4c43-af36-d16ebf308b73', '7da6e1fd-b935-493d-a5c4-8a04c4ae375d', 'BCT', 'https://assets.coingecko.com/coins/images/19240/small/bct_exchange_enhanced.png?1641441498', 'Toucan', 'Base Carbone Tonne');
INSERT INTO porto.transactions (id, price, date, token_amount, token_id, account_id, symbol, logo, project_name, token_name) VALUES ('ed9eea16-f871-4f27-a3e1-a782004ffe33', 41.25, '2023-03-17 19:35:37.220881 +00:00', 21.32, 'd8f6633a-5f01-49fe-a47a-daffb249f5e5', '7da6e1fd-b935-493d-a5c4-8a04c4ae375d', 'VITA', 'https://raw.githubusercontent.com/Uniswap/assets/master/blockchains/ethereum/assets/0x81f8f0bb1cB2A06649E51913A151F0E7Ef6FA321/logo.png', 'VitaDAO', 'VitaDAO');
INSERT INTO porto.transactions (id, price, date, token_amount, token_id, account_id, symbol, logo, project_name, token_name) VALUES ('1226e654-5121-4db1-9301-b04a8db0f3cd', 124.2, '2023-03-17 19:35:37.220881 +00:00', 9.23, '3387bd40-5554-4c43-af36-d16ebf308b73', '7da6e1fd-b935-493d-a5c4-8a04c4ae375d', 'BCT', 'https://assets.coingecko.com/coins/images/19240/small/bct_exchange_enhanced.png?1641441498', 'Toucan', 'Base Carbone Tonne');
INSERT INTO porto.transactions (id, price, date, token_amount, token_id, account_id, symbol, logo, project_name, token_name) VALUES ('569d83a6-1e61-4e3f-8b65-6cf634c6206f', 1.25, '2023-03-17 19:35:37.220881 +00:00', 221.32, 'd8f6633a-5f01-49fe-a47a-daffb249f5e5', '7da6e1fd-b935-493d-a5c4-8a04c4ae375d', 'VITA', 'https://raw.githubusercontent.com/Uniswap/assets/master/blockchains/ethereum/assets/0x81f8f0bb1cB2A06649E51913A151F0E7Ef6FA321/logo.png', 'VitaDAO', 'VitaDAO');

-- Privileges
GRANT ALL PRIVILEGES ON SCHEMA porto TO spring;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA porto TO spring;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA porto To spring;