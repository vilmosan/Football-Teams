/*CREATE TABLE library (
    library_id SERIAL,
    library_name text NOT NULL UNIQUE,
    library_postcode integer CHECK (library_postcode > 1000),
    CONSTRAINT library_pk PRIMARY KEY (library_id)
);

CREATE TABLE library_book (
    library_book_id SERIAL,
    ean text CHECK (ean LIKE '978%' AND length(ean) = 13),
    library_id integer NOT NULL REFERENCES library (library_id),
    title text NOT NULL,
    author text NOT NULL,
    page_number integer CHECK (page_number > 0),
    CONSTRAINT library_book_pk PRIMARY KEY (library_book_id)
);

ALTER TABLE library_book ADD COLUMN is_raktaron boolean;

update library_book set is_raktaron = true where library_book_id % 2 = 0;
update library_book set is_raktaron = false where is_raktaron is null;*/
create table club(
	club_id SERIAL,
	club_name text not null unique,
	club_established integer not null check(club_established >= 1862 and club_established <= 2019),
	club_country text not null,
	club_region text not null,
	club_city text not null,
	constraint club_pk primary key (club_id)
);
create type player_pos as enum ('Kapus', 'Védő', 'Középpályás', 'Csatár');

create table player(
	player_id SERIAL,
	player_name text not null,
    club_id integer NOT NULL REFERENCES club (club_id),
	player_license text not null unique,
	player_salary integer not null check(player_salary >= 150000),
	player_contract_start text not null,
	player_contract_expire text not null,
	player_position text not null,
	constraint player_pk primary key (player_id)
);


insert into club(club_name, club_established, club_country, club_region, club_city)
values ('Manchester United', '1878', 'Egyesült Királyság', 'Északnyugat-Anglia', 'Manchester');

insert into player(player_name, club_id, player_license, player_salary, player_contract_start, player_contract_expire, player_position)
values('Christiano Ronaldo', '1', '98734231', '590000', '2010-10-11', '2015-12-21', 'Csatár');

/** Enélkül 10es postgresen nem fut a save. Ez generálja az autoincrementing ID-t **/
CREATE SEQUENCE hibernate_sequence START 1;