DROP DATABASE IF EXISTS bah_games_db;
CREATE DATABASE bah_games_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bah_games_db;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_perfil ENUM('ADMINISTRADOR', 'CLIENTE') NOT NULL DEFAULT 'CLIENTE',
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao TEXT
);

CREATE TABLE jogo (
    id_jogo INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    data_lancamento DATE,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE RESTRICT
);

CREATE TABLE compra (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_jogo INT NOT NULL,
    valor_pago DECIMAL(10, 2) NOT NULL,
    data_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_jogo) REFERENCES jogo(id_jogo) ON DELETE CASCADE
);

INSERT INTO usuario (nome, email, senha, tipo_perfil) VALUES 
('Ryan  (Admin)', 'admin@bahgames.com', '1234', 'ADMINISTRADOR'),
('Jogador Teste', 'cliente@bahgames.com', '1234', 'CLIENTE');

INSERT INTO categoria (nome, descricao) VALUES 
('RPG', 'Jogos de interpretação de papéis com foco em narrativa e evolução.'),
('Ação e Aventura', 'Jogos dinâmicos com combate, exploração e desafios.'),
('Esportes', 'Simuladores e jogos competitivos de modalidades esportivas.'),
('Indie', 'Jogos independentes com mecânicas criativas e inovadoras.');

INSERT INTO jogo (titulo, descricao, preco, data_lancamento, id_categoria) VALUES 
('CyberPunk 2077', 'RPG futurista: uma distopia em 2077', 149.90, '2025-11-10', 1),
('Lego: Jairo Neto', 'Jogo lego que conta a história de Jairo.', 79.90, '2024-09-20', 2),
('EA Sports FC 26', 'Simulador de futebol com ligas mundiais.', 249.00, '2025-09-26', 3),
('Hollow Knight', 'Aventura sombria em um vasto reino subterrâneo de insetos.', 46.99, '2017-02-24', 4),
('Stardew Valley', 'Simulador de fazenda e vida no campo.', 24.99, '2016-02-26', 4);

INSERT INTO compra (id_usuario, id_jogo, valor_pago) VALUES 
(1, 3, 249.00),
(2, 4, 46.99),
(2, 5, 24.99);