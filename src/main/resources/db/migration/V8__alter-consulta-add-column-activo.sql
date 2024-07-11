ALTER TABLE consultas ADD activo tinyint default 1;
UPDATE consultas SET activo = 1;