ALTER TABLE consultas ADD motivo_cancelamiento varchar(100) default null;
UPDATE consultas SET motivo_cancelamiento = null;