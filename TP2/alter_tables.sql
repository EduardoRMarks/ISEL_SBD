
-- hora de abertura e hora de fecho podem ser null
ALTER TABLE `sbd_tp1_43498_45977_47739`.`horario` 
CHANGE COLUMN `HoraAbertura` `HoraAbertura` TIME NULL ,
CHANGE COLUMN `HoraFecho` `HoraFecho` TIME NULL ;

-- add coluna imagem para sala
ALTER TABLE `sbd_tp1_43498_45977_47739`.`sala` 
ADD COLUMN `Imagem` LONGTEXT NULL AFTER `Estado`;

-- add coluna imagem para equipamento
ALTER TABLE `sbd_tp1_43498_45977_47739`.`equipamento` 
ADD COLUMN `Imagem` LONGTEXT NULL AFTER `Estado`;
