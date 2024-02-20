-- Criação do Schema

CREATE SCHEMA `sbd_tp1_43498_45977_47739`;

-- Criação da tabela Utilizador
CREATE TABLE `sbd_tp1_43498_45977_47739`.`utilizador` (
    Email VARCHAR(100) PRIMARY KEY NOT NULL CONSTRAINT Emailutilizador_Regex CHECK(Email REGEXP '^[[:alnum:]._%+-]+@[[:alnum:].-]+\\.[[:alpha:]]{2,}$'),
    Password VARCHAR(255) NOT NULL,
    Role ENUM('Administrador','Pt','Cliente') NOT NULL
)DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;
	
-- Criação da tabela Tabela Clube
CREATE TABLE `sbd_tp1_43498_45977_47739`.`clube` (
  `Nif` INT NOT NULL CONSTRAINT NifClube_Regex CHECK(LENGTH(Nif) = 9),
  `Morada` VARCHAR(100) NOT NULL,
  `CodigoPostal` VARCHAR(8) NOT NULL CONSTRAINT CodigoPostal_Regex CHECK(CodigoPostal REGEXP '^[[:digit:]]{4}-[[:digit:]]{3}$'),
  `Localidade` VARCHAR(45) NOT NULL CONSTRAINT Localidade_Regex CHECK(Localidade REGEXP '[[:alpha:]- ]$'),
  `Telefone` VARCHAR(20) NOT NULL CONSTRAINT Telefone_Regex CHECK(Telefone REGEXP '^[+][[:digit:]]+ [[:digit:]]+$'),
  `DesignacaoComercial` VARCHAR(100) NOT NULL,
  `CoordenadasGeograficas` VARCHAR(100) NOT NULL CONSTRAINT CoordenadasGeograficas CHECK(CoordenadasGeograficas REGEXP '^[[:digit:]+\-º ]+$'),
  PRIMARY KEY (`Nif`)) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;

-- Criação da tabela Horario
CREATE TABLE `sbd_tp1_43498_45977_47739`.`horario` (
  `DiaDeSemana` ENUM('Segunda-Feira', 'Terça-Feira', 'Quarta-Feira', 'Quinta-Feira', 'Sexta-Feira', 'Sábado', 'Domingo'),
  `NifClube` INT NOT NULL,
  `HoraAbertura` TIME NOT NULL,
  `HoraFecho`TIME NOT NULL,
  PRIMARY KEY (`DiaDeSemana`, `NifClube`),
  INDEX `NifClube_idx` (`NifClube` ASC) VISIBLE,
  CONSTRAINT `NifClubeHorario`
    FOREIGN KEY (`NifClube`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`clube` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;
    
-- Criação da tabela Cliente
CREATE TABLE `sbd_tp1_43498_45977_47739`.`cliente` (
  `Nif` INT NOT NULL CONSTRAINT NifCliente_Regex CHECK(LENGTH(NIF) = 9),
  `Nome` VARCHAR(100) NOT NULL CONSTRAINT NomeCliente_Regex CHECK(Nome REGEXP '[[:alpha:]]'),
  `DataDeNascimento` DATE NOT NULL,
  `Telemovel` VARCHAR(20) NOT NULL CONSTRAINT TelemovelCliente_Regex CHECK(Telemovel REGEXP '^[+][[:digit:]]+ [[:digit:]]+$'),
  `Email` VARCHAR(100) NOT NULL CONSTRAINT EmailCliente_Regex CHECK(Email REGEXP '^[[:alnum:]._%+-]+@[[:alnum:].-]+\\.[[:alpha:]]{2,}$'),
  PRIMARY KEY (`Nif`),
	CONSTRAINT `Email_cliente_utilizador`
    FOREIGN KEY (`Email`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`utilizador` (`Email`)
	ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;


-- Criação da tabela PT
CREATE TABLE `sbd_tp1_43498_45977_47739`.`pt` (
  `Id` INT NOT NULL,
  `Nome` VARCHAR(100) NOT NULL CONSTRAINT NomePt_Regex CHECK(Nome REGEXP '[[:alpha:]]'),
  `Telemovel` VARCHAR(20) NOT NULL CONSTRAINT TelemovelPt_Regex CHECK(Telemovel REGEXP '^[+][[:digit:]]+ [[:digit:]]+$'),
  `Email` VARCHAR(100) NOT NULL CONSTRAINT EmailPt_Regex CHECK(Email REGEXP '^[[:alnum:]._%+-]+@[[:alnum:].-]+\\.[[:alpha:]]{2,}$'),
  `Fotografia` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`Id`),
  CONSTRAINT `Email_pt_utilizador`
    FOREIGN KEY (`Email`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`utilizador` (`Email`)
	ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;

-- Criação da tabela Patologia
CREATE TABLE `sbd_tp1_43498_45977_47739`.`patologia` (
  `Nome` VARCHAR(100) NOT NULL CONSTRAINT Nome_Patologia_Regex CHECK(Nome REGEXP '[[:alpha:]]'),
  `NifCliente` INT NOT NULL,
  PRIMARY KEY (`Nome`, `NifCliente`),
  INDEX `NifCliente_idx` (`NifCliente` ASC) VISIBLE,
  CONSTRAINT `NifClientePat`
    FOREIGN KEY (`NifCliente`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;

-- Criação da tabela Objetivo
CREATE TABLE `sbd_tp1_43498_45977_47739`.`objetivo` (
  `Nome` VARCHAR(100) NOT NULL CONSTRAINT NomeObjetivo_Regex CHECK(Nome REGEXP '[[:alpha:]]'),
  `NifCliente` INT NOT NULL,
  PRIMARY KEY (`Nome`, `NifCliente`),
  INDEX `NifCliente_idx` (`NifCliente` ASC) VISIBLE,
  CONSTRAINT `NifClienteObj`
    FOREIGN KEY (`NifCliente`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;

-- Criação da tabela Sala
CREATE TABLE `sbd_tp1_43498_45977_47739`.`sala` (
  `Id` INT NOT NULL,
  `NifClube` INT NOT NULL,
  `Nome` VARCHAR(100) NOT NULL CONSTRAINT NomeSala_Regex CHECK(Nome REGEXP '[[:alpha:]]'),
  `OcupacaoMaxima` INT NOT NULL,
  `Estado` BIT NOT NULL,
  PRIMARY KEY (`Id`, `NifClube`),
  INDEX `NifClubeSala_idx` (`NifClube` ASC) VISIBLE,
  CONSTRAINT `NifClubeSala`
    FOREIGN KEY (`NifClube`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`clube` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;
    
-- Criação da tabela equipamento
CREATE TABLE `sbd_tp1_43498_45977_47739`.`equipamento` (
  `Id` INT NOT NULL,
  `NifClube` INT NOT NULL,
  `Nome` VARCHAR(100) NOT NULL CONSTRAINT NomeEquipamento_Regex CHECK(Nome REGEXP '[[:alpha:]]'),
  `Demonstracao` VARCHAR(256) NOT NULL,
  `Estado` BIT NOT NULL,
  PRIMARY KEY (`Id`, `NifClube`),
  INDEX `NifClubeEquipamento_idx` (`NifClube` ASC) VISIBLE,
  CONSTRAINT `NifClubeEquipamento`
    FOREIGN KEY (`NifClube`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`clube` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;

-- Criação da tabela Atividade
CREATE TABLE `sbd_tp1_43498_45977_47739`.`atividade` (
  `Id` INT NOT NULL,
  `IdPt` INT NOT NULL,
  `Nome` VARCHAR(100) NOT NULL CONSTRAINT NomeAtividade_Regex CHECK(Nome REGEXP '[[:alpha:]]'), 
  `Estado` ENUM('Cancelado', 'InscricoesAbertas', 'InscricoesFechadas') NOT NULL,
  `Duracao` INT NOT NULL,
  `DiaDeSemana` ENUM('Segunda-Feira', 'Terça-Feira', 'Quarta-Feira', 'Quinta-Feira', 'Sexta-Feira', 'Sábado', 'Domingo') NULL,
  `HoraDeInicio` TIME NULL,
  `Tipo` ENUM('Semanal', 'Pontual') NOT NULL,
  `Data` DATETIME NULL,
  `MinParticipantes` INT NOT NULL,
  `MaxParticipantes` INT NOT NULL,
  `Confirmacao` BIT NOT NULL,
  `EscalaoEtario` ENUM('18-30', '31-45', '46-55', '56-70', '70+') NULL,
  `NifClube`INT NOT NULL,
  PRIMARY KEY (`Id`, `IdPt`),
  INDEX `IdPtAtiv_idx` (`IdPt` ASC) VISIBLE,
  CONSTRAINT `IdPtAtiv`
    FOREIGN KEY (`IdPt`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`pt` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `NifClubeAtiv`
    FOREIGN KEY (`NifClube`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`clube` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;
    
-- Criação da tabela clube_cliente
CREATE TABLE `sbd_tp1_43498_45977_47739`.`clube_cliente` (
  `NifClube` INT NOT NULL,
  `NifCliente` INT NOT NULL,
  PRIMARY KEY (`NifClube`, `NifCliente`),
  INDEX `NifCliente_idx` (`NifCliente` ASC) VISIBLE,
  CONSTRAINT `NifClubeCC`
    FOREIGN KEY (`NifClube`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`clube` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `NifClienteCC`
    FOREIGN KEY (`NifCliente`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;
	
-- Criação da tabela clube_pt
CREATE TABLE `sbd_tp1_43498_45977_47739`.`clube_pt` (
  `IdPt` INT NOT NULL,
  `NifClube` INT NOT NULL,
  PRIMARY KEY (`IdPt`, `NifClube`),
  INDEX `NifClubeCP_idx` (`NifClube` ASC) VISIBLE,
  CONSTRAINT `IdPtCP`
    FOREIGN KEY (`IdPt`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`pt` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `NifClubeCP`
    FOREIGN KEY (`NifClube`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`clube` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;


-- Criação da tabela cliente_atividade
CREATE TABLE `sbd_tp1_43498_45977_47739`.`cliente_atividade` (
  `NifCliente` INT NOT NULL,
  `IdAtividade` INT NOT NULL,
  `IdPt` INT NOT NULL,
  PRIMARY KEY (`NifCliente`, `IdAtividade`, `IdPt`),
  INDEX `IdAtividadeCA_idx` (`IdAtividade` ASC, `IdPt` ASC) VISIBLE,
  CONSTRAINT `NifClienteCA`
    FOREIGN KEY (`NifCliente`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `IdAtividadeCA`
    FOREIGN KEY (`IdAtividade` , `IdPt`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`atividade` (`Id` , `IdPt`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;

-- Criação da tabela atividade_equipamento
CREATE TABLE `sbd_tp1_43498_45977_47739`.`atividade_equipamento` (
  `IdAtividade` INT NOT NULL,
  `IdPt` INT NOT NULL,
  `IdEquipamento` INT NOT NULL,
  `NifClube` INT NOT NULL,
  PRIMARY KEY (`IdAtividade`, `IdPt`, `IdEquipamento`, `NifClube`),
  INDEX `IdEquipamentoAE_idx` (`IdEquipamento` ASC, `NifClube` ASC) VISIBLE,
  CONSTRAINT `IdAtividadeAE`
    FOREIGN KEY (`IdAtividade` , `IdPt`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`atividade` (`Id` , `IdPt`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `IdEquipamentoAE`
    FOREIGN KEY (`IdEquipamento` , `NifClube`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`equipamento` (`Id` , `NifClube`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;

-- Criação da tabela atividade_sala

CREATE TABLE `sbd_tp1_43498_45977_47739`.`atividade_sala` (
  `IdAtividade` INT NOT NULL,
  `IdPt` INT NOT NULL,
  `IdSala` INT NOT NULL,
  `NifClube` INT NOT NULL,
  PRIMARY KEY (`IdAtividade`, `IdPt`, `IdSala`, `NifClube`),
  INDEX `IdSalaAS_idx` (`IdSala` ASC, `NifClube` ASC) VISIBLE,
  CONSTRAINT `IdAtividadeAS`
    FOREIGN KEY (`IdAtividade` , `IdPt`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`atividade` (`Id` , `IdPt`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `IdSalaAS`
    FOREIGN KEY (`IdSala` , `NifClube`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`sala` (`Id` , `NifClube`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;

-- Criação da tabela pt_cliente_equipamento
CREATE TABLE `sbd_tp1_43498_45977_47739`.`pt_cliente_equipamento` (
  `IdPt` INT NOT NULL,
  `NifCliente` INT NOT NULL,
  `IdEquipamento` INT NOT NULL,
  `NifClube` INT NOT NULL,
  `Data` DATE NOT NULL,
  `Uso` BIT NOT NULL,
  PRIMARY KEY (`IdPt`, `NifCliente`, `IdEquipamento`, `NifClube`),
  INDEX `IdEquipamentoPCE_idx` (`NifClube` ASC, `IdEquipamento` ASC) VISIBLE,
  INDEX `NifClientePCE_idx` (`NifCliente` ASC) VISIBLE,
  CONSTRAINT `IdPtPCE`
    FOREIGN KEY (`IdPt`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`pt` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `NifClientePCE`
    FOREIGN KEY (`NifCliente`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `IdEquipamentoPCE`
    FOREIGN KEY (`NifClube` , `IdEquipamento`)
    REFERENCES `sbd_tp1_43498_45977_47739`.`equipamento` (`NifClube` , `Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) DEFAULT CHARSET=UTF8MB4 COLLATE=UTF8MB4_unicode_ci;
    
