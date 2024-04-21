# Project Overview
This project aims to develop a banking application that allows users to manage their finances, make transactions, and interact with their contacts seamlessly.

## UML Class Diagram

The UML Class Diagram represents the high-level design of the application's data model. It illustrates the relationships and attributes of the main entities involved in the system, including User, Account, Transaction, Contact, and Notification.

![UML Class Diagram](C:\Users\ningo\IdeaProjects\paymybuddy\bdd\uml\PMB_diagramme-de-classe.drawio.png)

## Physical Data Model

```sql
-- Create User table
CREATE TABLE Users (
    email VARCHAR(255) PRIMARY KEY UNIQUE NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create Account table
CREATE TABLE Account (
    id SERIAL PRIMARY key,
    user_email VARCHAR(255) NOT NULL,
    balance REAL DEFAULT 0,
    available_balance REAL DEFAULT 0,
    pending_balance REAL DEFAULT 0,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_email) REFERENCES Users(email)
);

-- Create Transaction table
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

-- Create Contact table
CREATE TABLE Contact (
    id SERIAL PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_email) REFERENCES Users(email)
);

-- Create Notification table
CREATE TABLE Notification (
    id serial PRIMARY KEY,
    user_email VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_email) REFERENCES Users(email)
);
```
The Physical Data Model defines the structure of the database tables required for the application. It outlines the relationships between entities and their respective attributes, providing a clear representation of the database schema.

