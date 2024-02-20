-- Adição de elementos à tabela utilizador
INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role) VALUES ('admin@monkey.pt', 'ZHSax6eFVT/HXEPW6ZFb7/Cdk09HanuBkB3NnrZuSQkH+E3X60hb0c+Z6hmev2Kx', 'Administrador'); -- admin
INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role) VALUES ('manuela@monkey.pt', 'hF6iJJKUvYIweFF5IH1Hu0y2gdPSRx5yKzLzfleQw9RImRgGOCGgfVl8l8cgX0uf', 'Pt'); -- manuela
INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role) VALUES ('bernardo@monkey.pt', 'uTZAGKS9Ffkle7E1Hbjd7bYOQmpsS91lBOXfcjlvl31AcpBIYuWoZ8S7JMQEM1G6', 'Pt'); -- bernardo
INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role) VALUES ('borat@monkey.pt', 'LKKk3fy2HIKLBVAnD6OAfe+BfFGUn0HO1AOy/UeNvQNzvAlNx5F50S5AQ7B++3us', 'Pt'); -- kazakhstan
INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role) VALUES ('eduardo@aol.com', 'iXv+1OJUjse7NB9qnj84ul/MT9lZ0PHAs65kMqKrhlis0iln+S9bKqRbcK3dLL7y', 'Cliente'); -- eduardo
INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role) VALUES ('roman@aol.com', 'YpnDw/n6yQ9VJPNvtrPB2mFZDuDpnnXdsjYwNnBh2EaypaguycZKFzdchLR60LVv', 'Cliente'); -- roman
INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role) VALUES ('nuno@aol.com', '1/18xR1MlKhuDIOcPJ20hELpzIvPNITLFHGYUYlmUsMNAeQar5qS6D/JRzZR3g7E', 'Cliente'); -- nuno


    -- Adição de elementos à tabela clube
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube` (`Nif`, `Morada`,`CodigoPostal`,`Localidade`, `Telefone`, `DesignacaoComercial`, `CoordenadasGeograficas`) VALUES ('505200597', 'Rua Joaquim Rocha Cabral, 26', '1600-075', 'Lisboa', '+351 211575055', 'Fitness Monkey Laranjeiras', '38.754885736502814, -9.166832646030144');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube` (`Nif`, `Morada`,`CodigoPostal`,`Localidade`, `Telefone`, `DesignacaoComercial`, `CoordenadasGeograficas`) VALUES ('505229590', 'Rua Raul Lino, 11', '2780-180', 'Oeiras', '+351 217405793', 'Fitness Monkey Oeiras', '38.68717437843238, -9.323478076716905');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube` (`Nif`, `Morada`,`CodigoPostal`,`Localidade`, `Telefone`, `DesignacaoComercial`, `CoordenadasGeograficas`) VALUES ('505492048', 'Avenida 25 de Abril, 1011', '2750-512', 'Cascais', '+351 219483930', 'Fitness Monkey Cascais Light', '38.6976012866616, -9.429356484656617');

-- Adição de elementos à tabela Horario
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Segunda-Feira', '505200597', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Segunda-Feira', '505229590', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Segunda-Feira', '505492048', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Terça-Feira', '505200597', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Terça-Feira', '505229590', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Terça-Feira', '505492048', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Quarta-Feira', '505200597', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Quarta-Feira', '505229590', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Quarta-Feira', '505492048', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Quinta-Feira', '505200597', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Quinta-Feira', '505229590', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Quinta-Feira', '505492048', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Sexta-Feira', '505200597', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Sexta-Feira', '505229590', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Sexta-Feira', '505492048', '07:00:00', '22:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Sábado', '505200597', '09:00:00', '20:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Sábado', '505229590', '09:00:00', '20:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Sábado', '505492048', '09:00:00', '20:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Domingo', '505200597', '10:00:00', '18:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Domingo', '505229590', '10:00:00', '18:00:00');
INSERT INTO `sbd_tp1_43498_45977_47739`.`horario`(`DiaDeSemana`, `NifClube`, `HoraAbertura`, `HoraFecho`) VALUES ('Domingo', '505492048', '10:00:00', '18:00:00');

-- Adição de elementos à tabela cliente
INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`, `Nome`, `DataDeNascimento`, `Telemovel`, `Email`) VALUES ('249550123', 'Roman Ishchuk', '1998-09-18', '+351 913057294', 'roman@aol.com');
INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`, `Nome`, `DataDeNascimento`, `Telemovel`, `Email`) VALUES ('229495027', 'Eduardo Marques', '1999-06-05', '+351 915839475', 'eduardo@aol.com');
INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`, `Nome`, `DataDeNascimento`, `Telemovel`, `Email`) VALUES ('271390585', 'Nuno Marques', '2000-07-26', '+351 913958589', 'nuno@aol.com');

-- Adição de elementos à tabela pt
INSERT INTO `sbd_tp1_43498_45977_47739`.`pt` (`Id`, `Nome`, `Telemovel`, `Email`, `Fotografia`) VALUES ('123', 'Manuela Gomes', '+351 919255839', 'manuela@monkey.pt', 'https://www.shutterstock.com/pt/image-photo/portrait-female-personal-trainer-holding-tablet-2249557387');
INSERT INTO `sbd_tp1_43498_45977_47739`.`pt` (`Id`, `Nome`, `Telemovel`, `Email`, `Fotografia`) VALUES ('456', 'Bernardo Santos', '+351 962049490', 'bernardo@monkey.pt', 'https://www.shutterstock.com/pt/image-photo/gym-fitness-portrait-proud-man-standing-2268100139');
INSERT INTO `sbd_tp1_43498_45977_47739`.`pt` (`Id`, `Nome`, `Telemovel`, `Email`, `Fotografia`) VALUES ('789', 'Borat Sagdiyev', '+351 934405759', 'borat@monkey.pt', 'https://www.nit.pt/wp-content/uploads/2020/09/bb4618219a55c0d215b3b96e0185773d-754x394.jpg');


-- Adição de elementos à tabela patologia
INSERT INTO `sbd_tp1_43498_45977_47739`.`patologia` (`Nome`, `NifCliente`) VALUES ('Asma', '229495027');
INSERT INTO `sbd_tp1_43498_45977_47739`.`patologia` (`Nome`, `NifCliente`) VALUES ('Diabetes', '249550123');
INSERT INTO `sbd_tp1_43498_45977_47739`.`patologia` (`Nome`, `NifCliente`) VALUES ('Tendinite', '249550123');
INSERT INTO `sbd_tp1_43498_45977_47739`.`patologia` (`Nome`, `NifCliente`) VALUES ('Tendinite', '271390585');

-- Adição de elementos à tabela objetivos
INSERT INTO `sbd_tp1_43498_45977_47739`.`objetivo` (`Nome`, `NifCliente`) VALUES ('Perda de Peso', '229495027');
INSERT INTO `sbd_tp1_43498_45977_47739`.`objetivo` (`Nome`, `NifCliente`) VALUES ('Ganhar Massa Muscular', '271390585');
INSERT INTO `sbd_tp1_43498_45977_47739`.`objetivo` (`Nome`, `NifCliente`) VALUES ('Definir Musculo', '249550123');


-- Adição de elementos à tabela Sala
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('1', '505200597', 'Piscina', '55', b'0');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('1', '505229590', 'Piscina', '70', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('1', '505492048', 'Sala de Box', '15', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('2', '505200597', 'Sala de musculacao', '25', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('2', '505229590', 'Sala de cardio', '25', b'0');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('2', '505492048', 'Sala de musculacao', '15', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('3', '505200597', 'Sala de cardio', '25', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('3', '505229590', 'Sala de musculacao', '25', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('3', '505492048', 'Estudio', '20', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`sala` (`Id`, `NifClube`, `Nome`, `OcupacaoMaxima`, `Estado`) VALUES ('4', '505200597', 'Estudio', '20', b'1');

-- Adição de elementos à tabela Equipamento
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('1', '505200597', 'Power Racks', 'https://www.youtube.com/watch?v=XtMRDXBH-Vg', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('1', '505229590', 'Power Racks', 'https://www.youtube.com/watch?v=XtMRDXBH-Vg', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('1', '505492048', 'Power Racks', 'https://www.youtube.com/watch?v=XtMRDXBH-Vg', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('2', '505200597', 'Supino', 'https://www.youtube.com/watch?v=WwXS2TeFmeg', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('2', '505229590', 'Supino', 'https://www.youtube.com/watch?v=WwXS2TeFmeg', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('2', '505492048', 'Supino', 'https://www.youtube.com/watch?v=WwXS2TeFmeg', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('3', '505200597', 'Leg Press', 'https://www.youtube.com/watch?v=NHcaiGLhEK8', b'0');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('3', '505229590', 'Leg Press', 'https://www.youtube.com/watch?v=NHcaiGLhEK8', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('3', '505492048', 'Leg Press', 'https://www.youtube.com/watch?v=NHcaiGLhEK8', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('4', '505200597', 'Passadeira', 'https://www.youtube.com/watch?v=SmUJ5NW1R9g', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('4', '505229590', 'Passadeira', 'https://www.youtube.com/watch?v=SmUJ5NW1R9g', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`equipamento` (`Id`, `NifClube`, `Nome`, `Demonstracao`, `Estado`) VALUES ('4', '505492048', 'Passadeira', 'https://www.youtube.com/watch?v=SmUJ5NW1R9g', b'1');

-- Adição de elementos à tabela Atividade 
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade` (`Id`, `IdPt`, `Nome`, `Estado`, `Duracao`, `DiaDeSemana`, `HoraDeInicio`, `Tipo`, `Data`, `MinParticipantes`, `MaxParticipantes`, `Confirmacao`, `EscalaoEtario`,`NifClube`) VALUES ('1', '123', 'Zumba', 'InscricoesAbertas', '50', 'Segunda-Feira', '09:00:00', 'Semanal', Null, '2', '10', b'0', '31-45','505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade` (`Id`, `IdPt`, `Nome`, `Estado`, `Duracao`, `DiaDeSemana`, `HoraDeInicio`, `Tipo`, `Data`, `MinParticipantes`, `MaxParticipantes`, `Confirmacao`, `EscalaoEtario`,`NifClube`) VALUES ('2', '456', 'Sessão com PT', 'InscricoesFechadas', '45', Null, Null, 'Pontual', '2023-11-25 15:30:00', '1', '1', b'1', Null,'505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade` (`Id`, `IdPt`, `Nome`, `Estado`, `Duracao`, `DiaDeSemana`, `HoraDeInicio`, `Tipo`, `Data`, `MinParticipantes`, `MaxParticipantes`, `Confirmacao`, `EscalaoEtario`,`NifClube`) VALUES ('3', '123', 'Hidroginastica', 'Cancelado', '30', Null, Null, 'Pontual', '2023-11-30 12:00:00', '3', '15', b'0', '70+','505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade` (`Id`, `IdPt`, `Nome`, `Estado`, `Duracao`, `DiaDeSemana`, `HoraDeInicio`, `Tipo`, `Data`, `MinParticipantes`, `MaxParticipantes`, `Confirmacao`, `EscalaoEtario`,`NifClube`) VALUES ('4', '789', 'Step', 'InscricoesAbertas', '60', 'Sábado', '11:00:00', 'Semanal', Null, '3', '10', b'0', '31-45','505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade` (`Id`, `IdPt`, `Nome`, `Estado`, `Duracao`, `DiaDeSemana`, `HoraDeInicio`, `Tipo`, `Data`, `MinParticipantes`, `MaxParticipantes`, `Confirmacao`, `EscalaoEtario`,`NifClube`) VALUES ('5', '123', 'Sessão com PT', 'InscricoesFechadas', '60', Null, Null, 'Pontual', '2023-11-28 10:00:00', '1', '1', b'1', Null,'505200597');

-- Adição de elementos à tabela clube_cliente
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_cliente` (`NifClube`, `NifCliente`) VALUES ('505200597', '229495027');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_cliente` (`NifClube`, `NifCliente`) VALUES ('505200597', '249550123');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_cliente` (`NifClube`, `NifCliente`) VALUES ('505200597', '271390585');


-- Adição de elementos à tabela clube_pt
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_pt` (`IdPt`, `NifClube`) VALUES ('123', '505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_pt` (`IdPt`, `NifClube`) VALUES ('456', '505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_pt` (`IdPt`, `NifClube`) VALUES ('789', '505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_pt` (`IdPt`, `NifClube`) VALUES ('123', '505229590');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_pt` (`IdPt`, `NifClube`) VALUES ('456', '505229590');
INSERT INTO `sbd_tp1_43498_45977_47739`.`clube_pt` (`IdPt`, `NifClube`) VALUES ('789', '505492048');

-- Adição de elementos à tabela cliente_atividade
INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente_atividade` (`NifCliente`, `IdAtividade`, `IdPt`) VALUES ('229495027', '1', '123');
INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente_atividade` (`NifCliente`, `IdAtividade`, `IdPt`) VALUES ('249550123', '1', '123');
INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente_atividade` (`NifCliente`, `IdAtividade`, `IdPt`) VALUES ('271390585', '1', '123');
INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente_atividade` (`NifCliente`, `IdAtividade`, `IdPt`) VALUES ('229495027', '2', '456');

-- Adição de elementos à tabela ativade_sala
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade_sala` (`IdAtividade`, `IdPt`, `IdSala`, `NifClube`) VALUES ('3', '123', '1', '505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade_sala` (`IdAtividade`, `IdPt`, `IdSala`, `NifClube`) VALUES ('2', '456', '3', '505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade_sala` (`IdAtividade`, `IdPt`, `IdSala`, `NifClube`) VALUES ('1', '123', '4', '505200597');


-- Adição de elementos à tabela pt cliente equipamento
INSERT INTO `sbd_tp1_43498_45977_47739`.`pt_cliente_equipamento` (`IdPt`, `NifCliente`, `IdEquipamento`, `NifClube`, `Data`, `Uso`) VALUES ('123', '229495027', '4', '505200597', '2023-11-11', b'0');
INSERT INTO `sbd_tp1_43498_45977_47739`.`pt_cliente_equipamento` (`IdPt`, `NifCliente`, `IdEquipamento`, `NifClube`, `Data`, `Uso`) VALUES ('123', '271390585', '1', '505200597', '2023-11-11', b'1');
INSERT INTO `sbd_tp1_43498_45977_47739`.`pt_cliente_equipamento` (`IdPt`, `NifCliente`, `IdEquipamento`, `NifClube`, `Data`, `Uso`) VALUES ('123', '271390585', '2', '505200597', '2023-11-11', b'1');

-- Adição de elementos à tabela atividade equipamento

INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade_equipamento` (`IdAtividade`, `IdPt`, `IdEquipamento`, `NifClube`) VALUES ('3','123','1','505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade_equipamento` (`IdAtividade`, `IdPt`, `IdEquipamento`, `NifClube`) VALUES ('5','123','2','505200597');
INSERT INTO `sbd_tp1_43498_45977_47739`.`atividade_equipamento` (`IdAtividade`, `IdPt`, `IdEquipamento`, `NifClube`) VALUES ('2','456','4','505200597');