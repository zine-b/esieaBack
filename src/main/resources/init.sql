create database stockcar;
drop table voiture;
create table voiture (
	id integer primary key auto_increment,	
	marque varchar(30) not null,
	modele varchar(30) not null,
	finition varchar(30) not null,
	carburant char,
	km integer,
	annee integer,
	prix integer
);


insert into voiture (marque, modele, finition, carburant, km, annee, prix) values
('Citroën', 'C4 Picasso', 'Feel', 'D', 78000, 2017, 15500),
('Peugeot', '3008', 'Allure', 'D', 4, 2020, 38000),
('Renault', 'Mégane', 'Dynamique', 'E', 133000, 2007, 3100),
('Opel', 'Corsa', 'Elegance', 'E', 140, 0, 0),
('Audi', 'R8', 'Black edition', 'E', 68000, 2009, 50000),
('BMW', 'F30', 'Lounge', 'E', 210000, 2014, 23990),
('Renault', 'VelSatis', 'Initiale', 'D', 174000, 2006, 3000),
('Volvo', 'V60', 'Inscription', 'D', 80674, 2016, 12890),
('DS', 'DS5', 'Sport chic', 'H', 111111, 2018, 14590);