/*
/ @author Anton Romanov
/ @date 07/10/2012
/ @version 1.2
*/



drop sequence Warteliste_ID_seq;
drop sequence Kind_ID_seq;
drop sequence Gruppe_ID_seq;
drop sequence Tageszeit_ID_seq;
drop sequence Kita_ID_seq;
drop sequence Sonderleistung_ID_seq;
drop sequence rechnung_id_seq;
drop sequence KLeiter_ID_seq;
drop sequence Elternteil_ID_seq;

drop table KindGruppe;
drop type rechnung_nested_type;
drop table Sonderleistung;
drop table Warteliste;
drop table Kind;
drop table Elternteil;
drop table Gruppe;
drop table Kita;
drop table KLeiter;
drop table Bundesland;
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

create table KLeiter(
 ID integer not null constraint PK_KLeiter primary key,
 Vorname varchar2(100) not null,
 Nachname varchar2(100) not null,
 Benutzername varchar2(100) not null,
 Passwort varchar2(100) not null
);

create table Bundesland(
  ID integer not null constraint PK_Bundesland primary key,
  Krzl varchar2(2) not null,
  Bezeichnung varchar2(50) not null
);

create table Kita (
  ID integer not null constraint PK_Kita primary key,
  Bezeichnung varchar2(100) not null,
  KLeiter integer not null constraint FK_Kita_KLeiter references KLeiter(ID),
  Bundesland integer not null constraint FK_Kita_Bundesland references Bundesland(ID)
);

create table Gruppe (
 ID integer not null constraint PK_Gruppe primary key,
 Bezeichnung varchar2(100),
 Tageszeit integer not null constraint FK_Gruppe_Tageszeit references Tageszeit(ID),
 Kita integer not null constraint FK_Gruppe_Kita references Kita(ID),
 Stunden number not null,
 constraint CK_Gruppe_Stunden check(Stunden>=0)
) cluster GruppeKind(ID);

create table Elternteil(
  ID integer not null constraint PK_Eltern primary key,
  Vorname varchar2(100) not null,
  Nachname varchar2(100) not null,
  Geschlecht varchar2(1) not null,
  constraint CK_Geschlecht check (Geschlecht in ('m','w')),
  Gehalt number(*,2) not null,
  constraint CK_Gehalt check (Gehalt>=0),
  Benutzername varchar2(100) not null,
  Passwort varchar2(100) not null
);

create table Kind (
 ID integer not null constraint PK_Kind primary key,
 Vorname varchar2(100) not null,
 Nachname varchar2(100) not null,
 Geburtsdatum date not null,
 Gehalt number(*,2) not null,
 constraint CK_Kind_Gehalt check (Gehalt > 0),
 Familie number not null,
 constraint CK_Kind_Familie check (Familie>=2),
 Elternteil integer not null constraint FK_Kind_Elternteil references Elternteil(ID)
) cluster GruppeKind(ID);

create table Warteliste (
 ID integer not null constraint PK_Warteliste primary key,
 Kind integer not null constraint FK_Warteliste_Kind references Kind(Id),
 Gruppe integer not null constraint FK_Warteliste_Gruppe references Gruppe(ID)
);

create table Sonderleistung(
  ID integer not null constraint PK_Sonderleistung primary key,
  Bezeichnung varchar2(50) not null,
  Preis number(*,2) not null,
  constraint CK_Sonderl_Preis check (Preis>0)
);

create or replace
type rechnung_type as object(
  Id number,
  Datum date,
  Betrag number
);
/

create or replace
type rechnung_nested_type as table of rechnung_type;
/

create table KindGruppe (
 Kind integer not null constraint FK_KindGruppe_Kind references Kind(ID),
 Gruppe integer not null constraint FK_KindGruppe_Gruppe references Gruppe(ID),
 Rechnungen rechnung_nested_type,
 Sonderleistung integer constraint FK_KindGruppe references Sonderleistung(ID),
 constraint PK_KindGruppe primary key (Kind,Gruppe)
) NESTED TABLE Rechnungen STORE AS KindGruppe_Rechnungen,
cluster GruppeKind(Gruppe);


/*
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
*/

/*
/   Sequenzen für IDs
*/
create sequence Elternteil_ID_seq start with 1 increment by 1;
create sequence KLeiter_ID_seq start with 1 increment by 1;
create sequence Kind_ID_seq start with 1 increment by 1;
create sequence Warteliste_ID_seq start with 1 increment by 1;
create sequence Gruppe_ID_seq start with 1 increment by 1;
create sequence Tageszeit_ID_seq start with 1 increment by 1;
create sequence Kita_ID_seq start with 1 increment by 1;
create sequence Sonderleistung_ID_seq start with 1 increment by 1;
create sequence rechnung_id_seq;

/*
/   Trigger zum Hochzählen von IDs
*/
create or replace trigger KLeiter_id_trigger
  before insert on KLeiter
  for each row
  begin
    select KLeiter_ID_seq.nextval into :new.id from dual;
  end;
/

create or replace trigger Elternteil_id_trigger
  before insert on Elternteil
  for each row
  begin
    select Elternteil_ID_seq.nextval into :new.id from dual;
  end;
/

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
create or replace
function getPriceByID( kid IN integer) return integer as
  gehalt number;
  familie number;
  stunden number;
begin
  select Gehalt into gehalt from Kind where ID=kid;
  select Familie into familie from Kind where ID=kid;
  select Stunden into stunden from Gruppe where Gruppe.ID=(select Gruppe from KindGruppe where KindGruppe.Kind=kid);
  return getPriceByValues(gehalt,stunden,familie);
end getPriceByID;
/

create or replace
function getPriceByValues( Gehalt IN number, Stunden IN number, Familie in number) return number is
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
    execute immediate 'select '||f||' from (select '|| f ||' from PreiseE where Netto>='||Gehalt||' order by '||f||' asc) where rownum=1' into preis;
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
/
*/
create or replace
function getKindIdByRechnungId(rechnung_id in number) return number as
  kind_id number;
  gruppe_id number;
  temp number;
  cursor csr is select Kind,Gruppe from KindGruppe;
begin
  open csr;
  loop
    fetch csr into kind_id, gruppe_id;
    exit when csr%notfound;
    select id into temp from the(select Rechnungen from KindGruppe where Kind=kind_id and Gruppe=gruppe_id);
    if rechnung_id = temp then return kind_id;
    end if;
  end loop;
end getKindIdByRechnungId;
/

/*
/ Testdaten
*/
insert into Bundesland(ID,Krzl,Bezeichnung) values(1,'HH','Hamburg');
insert into KLeiter(ID,Vorname,Nachname,Benutzername,Passwort) values(NULL,'Bruce','Lee','blee','12345');
insert into Kita values(NULL,'Kita Bauerberg',1,1);
insert into Tageszeit values(NULL,'vormittags');
insert into Tageszeit values(NULL,'nachmittags');
insert into Tageszeit values(NULL,'ganztags');
insert into Gruppe values(NULL,'Katzen',1,1,4);
insert into Elternteil(ID,Vorname,Nachname,Geschlecht,Gehalt,Benutzername,Passwort) values(NULL,'M','Wolowitz','w',1200,'mwolowitz','12345');
insert into Kind values(NULL,'Howard','Wolowitz','01.01.2010',2500.00,2,1);
--rechnung_nested_type(rechnung_type(1,to_date('18.10.2012','DD.MM.YYYY'),350))
insert into KindGruppe values(1,1,rechnung_nested_type(rechnung_type(1,to_date('18.10.2012','DD.MM.YYYY'),350)),NULL);

/*
insert into Kind values(NULL,'Max','Musterman','01.01.2010',1500.00,3);
insert into Kind values(NULL,'Max','Plank','08.10.1978',890.00,4);
insert into Kind values(NULL,'Erwin','Schrödinger','12.09.1887',890.00,4);
insert into Kind values(NULL,'Sheldon Lee','Cooper','07.05.1879',890.00,4);
insert into Warteliste(ID,Kind,Gruppe) values(NULL,3,1);
insert into Warteliste(ID,Kind,Gruppe) values(NULL,4,1);
insert into Warteliste(ID,Kind,Gruppe) values(NULL,5,1);
*/