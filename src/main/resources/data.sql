INSERT INTO users(email, firstname, lastname, password, role)
VALUES
    ('test', 'test', 'test', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('clara@mail.com', 'Clara', 'Kent', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('patrick@mail.com', 'Patrick', 'Dupond', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('david@mail.com', 'David', 'Todd', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('john@mail.com', 'John', 'Smith', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('erick@mail.com', 'Erick', 'Parse', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('maxime@mail.com', 'Maxime', 'Bond', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('jonathan@mail.com', 'Jonathan', 'Port', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('franck@mail.com', 'Franck', 'Lucas', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER'),
    ('luc@mail.com', 'Luc', 'Valet', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER');

INSERT INTO contact(email)
VALUES
    ('test'),
    ('clara@mail.com'),
    ('patrick@mail.com'),
    ('david@mail.com'),
    ('john@mail.com'),
    ('erick@mail.com'),
    ('maxime@mail.com'),
    ('jonathan@mail.com'),
    ('franck@mail.com'),
    ('luc@mail.com');

INSERT INTO user_contact (user_id, contact_id)
SELECT u.id, c.id
FROM users u
    CROSS JOIN (
    SELECT id
    FROM contact
    ORDER BY RANDOM()
    LIMIT 5
    ) c;

INSERT INTO account(available_balance, balance, id, pending_balance, user_id)
VALUES
    (5000, 5000, 1, 5000, 1),
    (5000, 5000, 2, 5000, 2),
    (5000, 5000, 3, 5000, 3),
    (5000, 5000, 4, 5000, 4),
    (5000, 5000, 5, 5000, 5),
    (5000, 5000, 6, 5000, 6),
    (5000, 5000, 7, 5000, 7),
    (5000, 5000, 8, 5000, 8),
    (5000, 5000, 9, 5000, 9),
    (5000, 5000, 10, 5000, 10);
