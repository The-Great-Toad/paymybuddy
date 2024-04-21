CREATE TABLE Notification (
    id serial PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_email) REFERENCES Users(email)
);