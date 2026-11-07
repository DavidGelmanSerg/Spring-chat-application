CREATE TABLE chats (
    id SERIAL PRIMARY KEY,
    type CHARACTER VARYING(30) NOT NULL,
    name CHARACTER VARYING(50),
    description CHARACTER VARYING(1000),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE chat_users (
    user_id INTEGER NOT NULL,
    chat_id INTEGER NOT NULL,
    role CHARACTER VARYING(50),
    PRIMARY KEY(user_id, chat_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE
);