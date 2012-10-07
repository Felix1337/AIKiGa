/*
/ @author Anton Romanov
/ @date 04/10/2012
/ @version 1.1
*/


drop sequence Warteliste_ID_seq;
drop sequence Kind_ID_seq;
drop sequence Gruppe_ID_seq;
drop sequence Tageszeit_ID_seq;
drop sequence Kita_ID_seq;
drop sequence Sonderleistung_ID_seq;

drop table KindGruppe;
drop table Sonderleistung;
drop table Warteliste;
drop table Kind;
drop table Gruppe;
drop table Kita;
drop table Tageszeit;

drop cluster GruppeKind;
/*
/ Cluster für Grupper->Kinder-Beziehung
*/
create cluster GruppeKind(Kind integer);
create index Idx_GruppeKind on cluster GruppeKind;


/*
/ Tabellen
*/

create table Tageszeit (
 ID integer not null constraint PK_Tageszeit primary key,
 Bezeichnung varchar2(100) not null
);

create table Kita (
  ID integer not null constraint PK_Kita primary key,
  Bezeichnung varchar2(100) not null
);

create table Gruppe (
 ID integer not null constraint PK_Gruppe primary key,
 Bezeichnung varchar2(100),
 Tageszeit integer not null constraint FK_Gruppe_Tageszeit references Tageszeit(ID),
 Kita integer not null constraint FK_Gruppe_Kita references Kita(ID)
) cluster GruppeKind(ID);

create table Kind (
 ID integer not null constraint PK_Kind primary key,
 Vorname varchar2(100) not null,
 Nachname varchar2(100) not null,
 Geburtsdatum date not null,
 Gehalt number(*,2) not null,
 constraint CK_Kind_Gehalt check (Gehalt > 0)
);

create table Warteliste (
 ID integer not null constraint PK_Warteliste primary key,
 Kind integer not null constraint FK_Warteliste_Kind references Kind(Id),
 Gruppe integer not null constraint FK_Warteliste_Gruppe references Gruppe(ID)
);

create table Sonderleistung(
  ID integer not null constraint PK_Sonderleistung primary key,
  Bezeichnung varchar2(50) not null
);

create table KindGruppe (
 Kind integer not null constraint FK_KindGruppe_Kind references Kind(ID),
 Gruppe integer not null constraint FK_KindGruppe_Gruppe references Gruppe(ID),
 Preis number(*,2) not null,
 constraint CK_KindGruppe_Preis check(Preis>0),
 Sonderleistung integer constraint FK_KindGruppe references Sonderleistung(ID),
 constraint PK_KindGruppe primary key (Kind,Gruppe)
) cluster GruppeKind(Gruppe);

create table PreiseA (
 Netto decimal not null constraint PK_PreiseA primary key,
 Zwei decimal not null,
 Drei decimal not null,
 Vier decimal not null,
 Fuenf decimal not null,
 Sechs decimal not null
);

create table PreiseB (
 Netto decimal not null constraint PK_PreiseB primary key,
 Zwei decimal not null,
 Drei decimal not null,
 Vier decimal not null,
 Fuenf decimal not null,
 Sechs decimal not null
);

create table PreiseC (
 Netto decimal not null constraint PK_PreiseC primary key,
 Zwei decimal not null,
 Drei decimal not null,
 Vier decimal not null,
 Fuenf decimal not null,
 Sechs decimal not null
);

create table PreiseD (
 Netto decimal not null constraint PK_PreiseD primary key,
 Zwei decimal not null,
 Drei decimal not null,
 Vier decimal not null,
 Fuenf decimal not null,
 Sechs decimal not null
);

create table PreiseE (
 Netto decimal not null constraint PK_PreiseE primary key,
 Zwei decimal not null,
 Drei decimal not null,
 Vier decimal not null,
 Fuenf decimal not null,
 Sechs decimal not null
);


/*
/   Sequenzen für IDs
*/
create sequence Kind_ID_seq start with 1 increment by 1;
create sequence Warteliste_ID_seq start with 1 increment by 1;
create sequence Gruppe_ID_seq start with 1 increment by 1;
create sequence Tageszeit_ID_seq start with 1 increment by 1;
create sequence Kita_ID_seq start with 1 increment by 1;
create sequence Sonderleistung_ID_seq start with 1 increment by 1;
/*
/   Trigger zum Hochzählen von IDs
*/
create or replace trigger Warteliste_id_trigger
  before insert on Warteliste
  for each row
  begin
    select Warteliste_ID_seq.nextval into :new.id from dual;
  end;
/

create or replace trigger Kind_id_trigger
  before insert on Kind
  for each row
  begin
    select Kind_ID_seq.nextval into :new.id from dual;
  end;
/

create or replace trigger Gruppe_id_trigger
  before insert on Gruppe
  for each row
  begin
    select Gruppe_ID_seq.nextval into :new.id from dual;
  end;
/

create or replace trigger Tageszeit_id_trigger
  before insert on Tageszeit
  for each row
  begin
    select Tageszeit_ID_seq.nextval into :new.id from dual;
  end;
/

create or replace trigger Kita_id_trigger
  before insert on Kita
  for each row
  begin
    select Kita_ID_seq.nextval into :new.id from dual;
  end;
/
create or replace trigger Sonderleistung_id_trigger
  before insert on Sonderleistung
  for each row
  begin
    select Sonderleistung_ID_seq.nextval into :new.id from dual;
  end;
/

/*
/   Trigger zum Löschen des Kindes aus der Warteliste, wenn es in die Gruppe hinzugefügt wird
*/
create or replace
trigger KindGruppe_Wartel_trigger
  after insert on KindGruppe
  for each row
  begin
      delete * from Warteliste w where w.Kind = :new.Kind and w.Gruppe in (select g.ID from Gruppe g where g.Tageszeit=(select Tageszeit from Gruppe where Gruppe.ID = w.Gruppe));
  end;
/
alter trigger KindGruppe_Wartel_trigger disable;
/*
/   Trigger zum Prüfen, ob die Gruppe voll ist
*/
create or replace
trigger Gruppe_Anzahl_trigger
  before insert on Gruppe for each row
  declare
    maxanzahl number;
    anzahl_aktuell number;
  begin
    maxanzahl := 30;
    select count(*) into anzahl_aktuell from Gruppe where ID=:new.ID;
    if anzahl_aktuell > maxanzahl then Raise_application_error(-20001,'Die Gruppe ist voll'); end if;
  end;
/

/*
/   Functions zum Ermitteln der Gebühren
*/
create or replace function getPriceByID( Kind IN integer) return integer as
  preis number;
begin
  preis:=0; -- select ....
  return preis;
end getPriceByID;
/
create or replace
function getPriceByValues( Gehalt IN number, Stunden IN integer, Familie in integer) return number is
  preis number(38,0);
  f varchar2(20);
  s varchar2(200);
begin
   case
    WHEN Familie=2 THEN f:='Zwei';
    when Familie=3 Then f:='Drei';
    when Familie=4 then f:='Vier';
    when Familie=5 then f:='Fuenf';
    when Familie>=6 then f:='Sechs';
  end case f;
  if Stunden>0 and Stunden<=4 then 
    select * into preis from (select f from PreiseE where Netto>= Gehalt order by f asc) where rownum=1;
  elsif Stunden>4 and Stunden<=6 then
    execute immediate 'select '||f||' from (select '|| f ||' from PreiseD where Netto>='||Gehalt||' order by '||f||' asc) where rownum=1' into preis;
  elsif Stunden>6 and Stunden<=8 then
     execute immediate 'select '||f||' from (select '|| f ||' from PreiseC where Netto>='||Gehalt||' order by '||f||' asc) where rownum=1' into preis;
  elsif Stunden>8 and Stunden<=10 then
     execute immediate 'select '||f||' from (select '|| f ||' from PreiseB where Netto>='||Gehalt||' order by '||f||' asc) where rownum=1' into preis;
  elsif Stunden>10 and Stunden<=12 then
     execute immediate 'select '||f||' from (select '|| f ||' from PreiseA where Netto>='||Gehalt||' order by '||f||' asc) where rownum=1' into preis;
  end if;
  return preis;
end getPriceByValues;
/

/*
/ Testdaten
*/
insert into Kita values(NULL,'Kita Bauerberg');
insert into Tageszeit values(NULL,'vormittags');
insert into Tageszeit values(NULL,'nachmittags');
insert into Tageszeit values(NULL,'ganztags');
insert into Gruppe values(NULL,'Katzen',1,1);
insert into Kind values(NULL,'Anton','Romanov','01.01.2010',2500.00);
insert into KindGruppe values(1,1,500,NULL);

select Gruppe.ID, count(*) as Anzahl from Kind, Gruppe, KindGruppe where Kind.ID=KindGruppe.Kind and Gruppe.ID=KindGruppe.Gruppe group by Gruppe.ID;