CREATE TABLE Transactions (
    id SERIAL PRIMARY KEY,
    id_sender_account INT NOT NULL,
    id_receiver_account INT NOT NULL,
    amount REAL NOT NULL,
    fee REAL NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_sender_account) REFERENCES Account(id),
    FOREIGN KEY (id_receiver_account) REFERENCES Account(id)
);
