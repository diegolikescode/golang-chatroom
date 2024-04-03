CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE pessoas (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    apelido VARCHAR(32) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    nascimento VARCHAR(15) NOT NULL,
    stack VARCHAR(300),
    campo_query VARCHAR(300) NOT NULL
);
