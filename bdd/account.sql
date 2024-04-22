CREATE TABLE Account (
     id SERIAL PRIMARY key,
     user_email VARCHAR(255) NOT NULL,
     balance REAL DEFAULT 0,
     available_balance REAL DEFAULT 0,
     pending_balance REAL DEFAULT 0,
     creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (user_email) REFERENCES Users(email)
);
