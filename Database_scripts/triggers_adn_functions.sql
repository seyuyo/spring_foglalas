create or replace NONEDITIONABLE TRIGGER arajanlat_beszuras
BEFORE INSERT OR UPDATE ON arajanlat
FOR EACH ROW
DECLARE
    v_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM repulojarat WHERE jaratszam = :NEW.jaratszam;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'A külső kulcs nem létezik a másik táblában!');
    END IF;
END;
create or replace NONEDITIONABLE TRIGGER biztositas_beszuras
BEFORE INSERT OR UPDATE ON biztositasi_csomag
FOR EACH ROW
DECLARE
    v_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM biztosito WHERE biztosito_kod = :NEW.biztosito_kod;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'A külső kulcs nem létezik a másik táblában!');
    END IF;
END;
create or replace NONEDITIONABLE TRIGGER kedvezmeny
BEFORE INSERT ON szalloda_foglalas
FOR EACH ROW
DECLARE
  v_count NUMBER;
  aktkedvezmeny NUMBER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM arajanlat WHERE jaratszam = :NEW.szalloda_id;

  IF v_count > 0 THEN
    SELECT kedvezmeny INTO aktkedvezmeny FROM arajanlat WHERE jaratszam = :NEW.szalloda_id;
    if aktkedvezmeny > 0 THEN
        :NEW.ar := :NEW.ar*((100-aktkedvezmeny)/100);
    END IF;
  END IF;
END;
create or replace NONEDITIONABLE TRIGGER repulojarat_beszuras
BEFORE INSERT OR UPDATE ON repulojarat
FOR EACH ROW
DECLARE
    v_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM legitarsasag WHERE legitarsasag_kod = :NEW.legitarsasag_kod;
    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'A külső kulcs nem létezik a másik táblában!');
    END IF;
END;
create or replace NONEDITIONABLE TRIGGER szallodafoglalasbeszuras
BEFORE INSERT ON szalloda_foglalas
FOR EACH ROW
BEGIN
    if :NEW.foglalas_kezdete >= :NEW.foglalas_vege THEN
        RAISE_APPLICATION_ERROR(-20001, 'A külső kulcs nem létezik a másik táblában!');
    END IF;
END;
create or replace NONEDITIONABLE FUNCTION RepFogFelhSzer(felhaszn_id IN INT) 
RETURN SYS_REFCURSOR AS
  my_cursor SYS_REFCURSOR;
BEGIN
  OPEN my_cursor FOR
    SELECT REPJEGY_FOGLALAS.REPJEGY_FOGLALAS_ID, REPULOJARAT.JARATSZAM, REPULOJARAT.INDULASI_VAROS, REPULOJARAT.ERKEZESI_VAROS, REPULOJARAT.INDULASI_IDOPONT, REPULOJARAT.ERKEZESI_IDOPONT, REPJEGY_FOGLALAS.FIZETVE, REPJEGY_FOGLALAS.JEGYDARAB, REPJEGY_FOGLALAS.JEGYTIPUS, REPJEGY_FOGLALAS.OSZTALY, LEGITARSASAG.NEV FROM REPULOJARAT, REPJEGY_FOGLALAS, LEGITARSASAG WHERE REPJEGY_FOGLALAS.felhaszn_ID=RepFogFelhSzer.felhaszn_id AND REPJEGY_FOGLALAS.JARATSZAM=REPULOJARAT.JARATSZAM AND REPULOJARAT.LEGITARSASAG_KOD=LEGITARSASAG.LEGITARSASAG_KOD; 
  RETURN my_cursor;
END; 
create or replace NONEDITIONABLE FUNCTION SzallFogFelhSzer(felhaszn_id IN INT) 
RETURN SYS_REFCURSOR AS
  my_cursor SYS_REFCURSOR;
BEGIN
  OPEN my_cursor FOR
    SELECT SZALLODA_FOGLALAS.SZALLODA_FOGLALAS_ID, SZALLODA.NEV, SZALLODA.VAROS, SZALLODA.CIM, SZALLODA_FOGLALAS.FOGLALAS_KEZDETE, SZALLODA_FOGLALAS.FOGLALAS_VEGE, SZALLODA_FOGLALAS.AR, SZALLODA_FOGLALAS.FO FROM SZALLODA, SZALLODA_FOGLALAS WHERE SZALLODA_FOGLALAS.SZALLODA_ID=SZALLODA.SZALLODA_ID AND SZALLODA_FOGLALAS.FELHASZN_ID=SzallFogFelhSzer.felhaszn_id; 
    RETURN my_cursor;
END;

CREATE OR REPLACE NONEDITIONABLE FUNCTION foglalasok_es_bevetel
    RETURN SYS_REFCURSOR
    IS
    v_foglalasok_szama NUMBER(10);
    v_bevetel NUMBER(10,2);
    my_cursor SYS_REFCURSOR;
BEGIN
    SELECT COUNT(*), SUM(a.ar) INTO v_foglalasok_szama, v_bevetel
    FROM Repjegy_foglalas r, Arajanlat a
    WHERE r.jaratszam = a.jaratszam
      AND r.foglalasi_idopont >= TRUNC(SYSDATE, 'YEAR')
      AND r.foglalasi_idopont < ADD_MONTHS(TRUNC(SYSDATE, 'YEAR'), 12);

    OPEN my_cursor FOR
        SELECT v_foglalasok_szama, v_bevetel FROM DUAL;
    RETURN my_cursor;
END;
