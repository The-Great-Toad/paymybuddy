CREATE TABLE Contact (
    id SERIAL PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_email) REFERENCES Users(email)
);
