# NileTel Self-Care

Final project: a legacy billing system (`bss-core`), a new service
(`selfcare-api`) and a website (`selfcare-web`).

## Status

| Part | State |
|---|---|
| `bss-core` | In progress |

## bss-core

Legacy billing system. Jakarta EE 10 on WildFly, JDBC, PostgreSQL.

### Requirements

- JDK 17, Maven 3.9+
- PostgreSQL 16+
- WildFly 35 (start with `standalone-full.xml`)
- PostgreSQL JDBC driver 42.7.x

### Run it

1. Create the databases (once, as the PostgreSQL administrator):
   `psql -h localhost -U postgres -v bss_pw=<password> -v sc_pw=<password> -f db/00-create-databases.sql`
2. Create the tables as `bss_app`:
   `psql -h localhost -U bss_app -d bss_core -f db/bss-core/01-ddl.sql`
3. Copy the driver jar into `WILDFLY_HOME/standalone/deployments/`.
4. Start WildFly: `bin\standalone.bat -c standalone-full.xml`
5. Create the data source (see `docs/wildfly-setup.md`).
6. Build and deploy: `cd bss-core && mvn clean package`, then copy
   `target/bss-core.war` into `standalone/deployments/`.
7. Check: `http://localhost:8080/bsscore/health` answers `UP`.

### Team

| Member | Area |
|---|---|
| Nour | Person 1: platform and identity (L-01, L-02, L-03, L-04, L-06, L-09, L-10, L-20) |