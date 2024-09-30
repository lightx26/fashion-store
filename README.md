How to run my server application:
- Run with default profile
- Config datasource in application.properties
- Make sure there is a database in spring.datasource.url

  After run my application successfully, liquibase will create tables in the database at your datasource, so you don't need to create table by yourself (such as import sql) <br />
  But then, the sample records must be imported manually.

  Notices:
  - If you already have your database tables complete and don't want to migrate table by liquibase, you can set spring.liquibase.enabled to false
  - Feature count how many people are watching on a product implement on a redis-server as a database, so if you don't have redis-server, that feature obviously does not working (it always returns 1)
  - You can config host and port of redis server in application.properties
