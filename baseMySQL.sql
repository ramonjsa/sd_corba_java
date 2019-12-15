CREATE DATABASE `banco` ;
CREATE TABLE `conta` (
  `idconta` int(11) NOT NULL,
  `saldo` float DEFAULT NULL,
  PRIMARY KEY (`idconta`)
)  ENGINE=InnoDB DEFAULT CHARSET=latin1;

SELECT saldo FROM conta where idconta = 1;

UPDATE banco.conta SET saldo=saldo+750.5 WHERE idconta=1;
SELECT * FROM banco.conta ;

UPDATE banco.conta SET saldo=saldo-750.5 WHERE idconta=1;
SELECT * FROM banco.conta ;

/*usuario corba , pw corba*/