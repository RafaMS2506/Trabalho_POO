CREATE DATABASE IF NOT EXISTS biblioteca_db;
USE biblioteca_db;

CREATE TABLE IF NOT EXISTS autor (
    codigo INT AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    nacionalidade VARCHAR(50),
    data_nascimento DATE,
    email VARCHAR(50),
    telefone VARCHAR(13),
    PRIMARY KEY (codigo)
);

CREATE TABLE IF NOT EXISTS leitor (
    codigo INT AUTO_INCREMENT,
    cpf VARCHAR(14) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    data_cadastro DATE NOT NULL,
    PRIMARY KEY (codigo)
);

CREATE TABLE IF NOT EXISTS livro (
    codigo INT AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    editora VARCHAR(100) NOT NULL,
    ano INT,
    isbn VARCHAR(20),
    quantidade INT DEFAULT 0,
    codigo_autor INT NOT NULL,
    PRIMARY KEY (codigo),
    FOREIGN KEY (codigo_autor) REFERENCES autor(codigo)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS emprestimo (
    codigo INT AUTO_INCREMENT,
    codigo_leitor INT NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (codigo),
    FOREIGN KEY (codigo_leitor) REFERENCES leitor(codigo)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS item_emprestimo (
    codigo_emprestimo INT NOT NULL,
    codigo_livro INT NOT NULL,
    data_devolucao DATE,
    status_item VARCHAR(20) NOT NULL,
    PRIMARY KEY (codigo_emprestimo, codigo_livro),
    FOREIGN KEY (codigo_emprestimo) REFERENCES emprestimo(codigo)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (codigo_livro) REFERENCES livro(codigo)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);
