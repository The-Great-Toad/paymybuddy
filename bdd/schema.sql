CREATE TABLE users (
    email VARCHAR(255) PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    balance DOUBLE PRECISION DEFAULT 0
);

CREATE TABLE contact (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    UNIQUE(email)
);

CREATE TABLE user_contact (
    user_id VARCHAR(255),
    contact_id INT,
    PRIMARY KEY (user_id, contact_id),
    FOREIGN KEY (user_id) REFERENCES users(email),
    FOREIGN KEY (contact_id) REFERENCES contact(id)
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    receiver_email VARCHAR(255),
    sender_email   VARCHAR(255),
    description VARCHAR(255),
    amount DOUBLE PRECISION NOT NULL,
    fee DOUBLE PRECISION NOT NULL,
    date TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);