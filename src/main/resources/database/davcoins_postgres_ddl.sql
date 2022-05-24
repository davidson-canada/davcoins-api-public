-- Create/recreate schema
-- CASCADE so it deletes child objects (i.e. tables)
DROP SCHEMA IF EXISTS davcoins_schema CASCADE;
CREATE SCHEMA IF NOT EXISTS davcoins_schema authorization "dav-coins-user";

-- Use davcoins_schema
SET search_path TO davcoins_schema;

CREATE TABLE user_balance (
    notion_user UUID,
    balance numeric(1000, 2) NOT NULL CHECK (balance >= 0) DEFAULT 0,
    PRIMARY KEY(notion_user)
);

--Create bank user.
INSERT INTO user_balance VALUES ('00000000-0000-0000-0000-000000000000', 0);

CREATE TABLE products (
    id serial primary key,
    name text NOT NULL CHECK (name <> ''),
    description text NOT NULL DEFAULT '',
    price numeric(1000, 2) NOT NULL CHECK (price >= 0),
    created_date Timestamp NOT NULL DEFAULT NOW(),
    modified_date Timestamp NOT NULL DEFAULT NOW()
);

INSERT INTO products (name, description, price) VALUES('Retrait', 'Retrait', 0);
INSERT INTO products (name, description, price) VALUES('Ajout', 'Ajout', 0);

CREATE TABLE transactions (
    id serial primary key,
    from_user UUID NOT NULL,
    to_user UUID NOT NULL,
    product_id integer,
    product_quantity integer NOT NULL CHECK (product_quantity > 0),
    transaction_amount numeric(1000, 2) NOT NULL CHECK (transaction_amount > 0),
    transaction_date Timestamp NOT NULL DEFAULT NOW(),
    transaction_description text NOT NULL DEFAULT '',
    constraint from_uuid
        FOREIGN KEY(from_user)
            REFERENCES user_balance(notion_user),
    constraint to_uuid
        FOREIGN KEY(to_user)
            REFERENCES user_balance(notion_user),
    constraint product_id
        FOREIGN KEY(product_id)
            REFERENCES products(id)
            ON DELETE SET NULL
);