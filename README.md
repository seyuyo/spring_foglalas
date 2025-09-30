# spring_foglalas

Egy Spring alapú foglalási rendszer backend, amely lehetővé teszi foglalások létrehozását, megtekintését, módosítását és törlését REST API-n keresztül.

## Leírás

A **spring_foglalas** alkalmazás célja, hogy szolgáltasson egy REST API-t foglalások kezelésére. Az alkalmazás lehetővé teszi ügyfelek számára, hogy foglalásokat hozzanak létre, lekérdezzék meglévő foglalásaikat, módosítsák vagy töröljék őket. A backend kezeli az adatbázist, a validációt és a kontrollereket.

## Funkciók

- Új foglalás létrehozása (POST)  
- Foglalások lekérdezése (GET) — egyedi és lista formában  
- Foglalás módosítása (PUT / PATCH)  
- Foglalás törlése (DELETE)  
- Validációs szabályok (pl. kötelező mezők, dátumok érvényessége)  
- Hibakezelés, válaszobjektumok állapotkódokkal  
- (Lehetséges bővítés) Felhasználói jogosultságok, autentikáció/autorizáció  

## Technológiák

| Komponens | Használt technológia |
|-----------|-----------------------|
| Keretrendszer | Spring Boot |
| Web / REST | Spring Web MVC / Spring Web |
| Adatkezelés | Spring Data JPA (valamilyen JPA implementáció, pl. Hibernate) |
| Adatbázis | H2 (fejlesztői mód) / MySQL / PostgreSQL / más relációs DB |
| Java verzió | Java 8 vagy újabb |
| Build rendszer | Maven vagy Gradle |
| JSON (request / response) | Jackson vagy más JSON könyvtár |
| Verziókezelés | Git / GitHub |
