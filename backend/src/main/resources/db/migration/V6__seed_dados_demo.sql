-- Dados de demonstracao para que a aplicacao nao suba vazia em um ambiente novo
-- (ex.: producao recem-provisionada no Supabase). Todos os INSERTs sao
-- idempotentes: rodar esta migration contra um banco que ja tenha esses
-- mesmos dados (ex.: banco de dev local) nao duplica nada.

INSERT INTO especies (nome_popular, nome_cientifico, status_conservacao)
VALUES
    ('Abelha Jataí', 'Tetragonisca angustula', 'POUCO_PREOCUPANTE'),
    ('Borboleta Monarca', 'Danaus plexippus', 'VULNERAVEL'),
    ('Beija-flor Tesoura', 'Eupetomena macroura', 'POUCO_PREOCUPANTE'),
    ('Mamangava', 'Bombus morio', 'QUASE_AMEACADA')
ON CONFLICT (nome_cientifico) DO NOTHING;

INSERT INTO regioes (nome, cidade, estado)
SELECT * FROM (VALUES
    ('Parque Estadual', 'São Paulo', 'SP'),
    ('Jardim Botânico', 'Curitiba', 'PR'),
    ('Mata Atlântica', 'Rio de Janeiro', 'RJ'),
    ('Amazonia', 'Manaus', 'AM')
) AS novo(nome, cidade, estado)
WHERE NOT EXISTS (
    SELECT 1 FROM regioes r WHERE r.nome = novo.nome AND r.cidade = novo.cidade
);

-- Contas de demonstracao (senha para ambas: polinizamap123)
INSERT INTO usuarios (nome, email, senha, role)
VALUES
    ('Visitante Demo', 'visitante@polinizamap.com', '$2a$10$falgjICi2HcCkrAw.CQTxeTAHHlXT92ntuLBRLp0gFRXrmI4z7nXm', 'CIDADAO'),
    ('Pesquisadora Demo', 'pesquisador.demo@polinizamap.com', '$2a$10$trDh0aEnUVtnEyVHFMtULugxDKh5AV5SGl67MPYsgZpPcuIVETKQS', 'PESQUISADOR')
ON CONFLICT (email) DO NOTHING;

INSERT INTO avistamentos (usuario_id, especie_id, regiao_id, latitude, longitude, descricao, data_hora, status, nota_validacao)
SELECT u.id, e.id, r.id, d.latitude, d.longitude, d.descricao, d.data_hora::timestamp, d.status, d.nota_validacao
FROM (VALUES
    ('Tetragonisca angustula', 'Parque Estadual', 'São Paulo', -23.5505, -46.6333, 'Vista coletando néctar em flores amarelas no início da manhã.', '2026-06-10 08:15:00', 'APROVADO', 'Identificação confirmada por foto.'),
    ('Danaus plexippus', 'Jardim Botânico', 'Curitiba', -25.4284, -49.2733, 'Borboleta pousada em arbusto de flores roxas.', '2026-06-15 14:30:00', 'APROVADO', 'Espécie confirmada.'),
    ('Eupetomena macroura', 'Mata Atlântica', 'Rio de Janeiro', -22.9068, -43.1729, 'Beija-flor visitando flores de ixora.', '2026-06-20 09:00:00', 'PENDENTE', NULL),
    ('Bombus morio', 'Amazonia', 'Manaus', -3.1190, -60.0217, 'Mamangava avistada próxima a uma plantação local.', '2026-06-22 16:45:00', 'PENDENTE', NULL),
    ('Tetragonisca angustula', 'Mata Atlântica', 'Rio de Janeiro', -22.9100, -43.2000, 'Possível abelha jataí em jardim urbano.', '2026-06-25 11:20:00', 'REJEITADO', 'Foto não permite confirmar a espécie com segurança.'),
    ('Danaus plexippus', 'Parque Estadual', 'São Paulo', -23.5600, -46.6400, 'Borboleta monarca em canteiro de flores.', '2026-06-28 15:10:00', 'APROVADO', 'Confirmado por especialista local.')
) AS d(nome_cientifico, regiao_nome, regiao_cidade, latitude, longitude, descricao, data_hora, status, nota_validacao)
JOIN especies e ON e.nome_cientifico = d.nome_cientifico
JOIN regioes r ON r.nome = d.regiao_nome AND r.cidade = d.regiao_cidade
JOIN usuarios u ON u.email = 'visitante@polinizamap.com'
WHERE NOT EXISTS (
    SELECT 1 FROM avistamentos a
    WHERE a.usuario_id = u.id
      AND a.especie_id = e.id
      AND a.regiao_id = r.id
      AND a.data_hora = d.data_hora::timestamp
);
