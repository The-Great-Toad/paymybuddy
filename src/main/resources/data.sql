INSERT INTO users(email, firstname, lastname, password, role, balance)
VALUES
    ('test@test.com', 'test', 'test', '$2a$10$bFfnHsj1di5AeMftBY4upu88zLZ43PPeSNKE5.n.N.aaNNv9hNIw.', 'USER', 5000),
    ('clara@mail.com', 'Clara', 'Kent', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000),
    ('patrick@mail.com', 'Patrick', 'Dupond', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000),
    ('david@mail.com', 'David', 'Todd', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000),
    ('john@mail.com', 'John', 'Smith', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000),
    ('erick@mail.com', 'Erick', 'Parse', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000),
    ('maxime@mail.com', 'Maxime', 'Bond', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000),
    ('jonathan@mail.com', 'Jonathan', 'Port', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000),
    ('franck@mail.com', 'Franck', 'Lucas', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000),
    ('luc@mail.com', 'Luc', 'Valet', '$2a$10$IzT8G1hYKJjRNlDr4HzlW.YgTwpUsijy0wKKYdRHKb9uQjOuBS1ea', 'USER', 5000);

INSERT INTO contact(email)
VALUES
    ('test@test.com'),
    ('clara@mail.com'),
    ('patrick@mail.com'),
    ('david@mail.com'),
    ('john@mail.com'),
    ('erick@mail.com'),
    ('maxime@mail.com'),
    ('jonathan@mail.com'),
    ('franck@mail.com'),
    ('luc@mail.com');

-- INSERT INTO user_contact (user_id, contact_id)
-- SELECT u.id, c.id
-- FROM users u
--     CROSS JOIN (
--     SELECT id
--     FROM contact
--     ORDER BY RANDOM()
--     LIMIT 5
--     ) c;
