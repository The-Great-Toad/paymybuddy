# Project Overview
This project aims to develop a banking application that allows users to manage their finances, make transactions, and interact with their contacts seamlessly.

## UML Class Diagram

The UML Class Diagram represents the high-level design of the application's data model. It illustrates the relationships and attributes of the main entities involved in the system, including User, Account, Transaction, Contact, and Notification.

![UML Class Diagram](C:\Users\ningo\IdeaProjects\OC-projects\OC-paymybuddy\bdd\uml\PMB_diagramme-de-classe.drawio.png)

## Physical Data Model

The Physical Data Model defines the structure of the database tables required for the application. It outlines the relationships between entities and their respective attributes, providing a clear representation of the database schema.

![Physical Data Model](C:\Users\ningo\IdeaProjects\OC-projects\OC-paymybuddy\bdd\uml\PMB_diagramme-de-modele-physique.png)

### Table creation script

```sql
-- Create User table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create Contact table
CREATE TABLE Contact (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    UNIQUE(email)
);

-- Create UserContact table for ManyToMany relationship
CREATE TABLE user_contact (
    user_id INT,
    contact_id INT,
    PRIMARY KEY (user_id, contact_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (contact_id) REFERENCES contact(id)
);

-- Create Account table
CREATE TABLE account (
    id SERIAL PRIMARY key,
    user_id INT UNIQUE NOT NULL,
    balance REAL DEFAULT 0,
    available_balance REAL DEFAULT 0,
    pending_balance REAL DEFAULT 0,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create Transaction table
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    amount REAL NOT NULL,
    fee REAL NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Account_Transaction table for ManyToMany relationship
CREATE TABLE Account_Transaction (
    account_id INT NOT NULL,
    transaction_id INT NOT NULL,
    PRIMARY KEY (account_id, transaction_id),
    FOREIGN KEY (account_id) REFERENCES Account(id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);


-- Create Notification table
CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(10) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```