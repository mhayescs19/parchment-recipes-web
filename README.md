# Development Docs
This guide will track the development of the parchment recipes Spring backend in Java.
## Configure Spring Initializr

## Installing MySQL Local Instance
Download MySQL community for your machine specifications, then install. On macOS, toggling of the database is visible in the system preferences panel.  

Next, access MySQL by using the `mysql` command. Note that MySQL is **not** immediately added to the `PATH` variable. Notice that using the following bash command will not work if you are not at `/usr/local/mysql-version_number-os-architecture`.
```sh
mysql -u root -p
```
Remediate the issue by adding `mysql` to the `PATH` and make the command accessible anywhere by using the following steps:
1. Access the zsh profile for editing.
      ```sh
      nano ~/.zshrc
      ```
2. Add the following path based on the specific naming of the mysql folder.
      ```sh
      # in zshrc file add
      export PATH=${PATH}:/usr/local/mysql-9.0.0-macos14-arm64/bin
      ```
3. Source the changes.
      ```sh
      source ~/.zshrc
      ```
Now, enter the root user password to access the MySQL interface. Upon success, the mysql interface will look similar to below:
```
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 8
Server version: 9.0.0 MySQL Community Server - GPL

Copyright (c) 2000, 2024, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>
```

Despite installing correctly and configuring a root password, you may recieve the following error: 
```sql
ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password: YES)
```
I faced this issue becuase I had a different version of MySQL in `/usr/local/`. Once I removed the duplicate instance, the root password from the configuration was accepted. If those steps do not work, you may need to change the root password using the following steps:
1. Stop the MySQL server in the systems panel.
2. Access MySQL in safe mode with the following command.
   ```sh
   sudo mysqld_safe --skip-grant-tables &
   ```
3. Login without a password.
   ```sh
   mysql -u root
   ```
4. Reset the password
   ```sql
   FLUSH PRIVILEGES;
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
   ```
Before adding a database, let's [create a new user](https://www.digitalocean.com/community/tutorials/how-to-create-a-new-user-and-grant-permissions-in-mysql#:~:text=Run%20the%20following%20command%20to%20create%20a%20user%20that%20authenticates%20with%20caching_sha2_password.%20Be%20sure%20to%20change%20sammy%20to%20your%20preferred%20username%20and%20password%20to%20a%20strong%20password%20of%20your%20choosing%3A) and give explicit permissions for clarity.
```sql
CREATE USER 'username'@'host' IDENTIFIED BY 'password';
GRANT CREATE, ALTER, INSERT, UPDATE, SELECT, DELETE, DROP ON *.* TO 'username'@'host'; -- allow create, edit, read, delete
```
Verify that the user has been created by listing all users. I created a new user parchment which is visible below:
```sql
SELECT User, Host FROM mysql.user;
+------------------+-----------+
| User             | Host      |
+------------------+-----------+
| mysql.infoschema | localhost |
| mysql.session    | localhost |
| mysql.sys        | localhost |
| parchment        | localhost |
| root             | localhost |
+------------------+-----------+
```
Exit mysql and login to the new user by entering the user password when prompted.
```sql
mysql -u username -p
```
Before creating configuring a database, ensure your MySQL server is listening on the correct port (the default is 3306). I had to reinitialize the server via the system preferences panel to allow login and for the server to listen on the correct port.
```sql
SHOW GLOABL VARIABLES LIKE 'port';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| port          | 3306  | -- if the port is 0, the server is improperly configured
+---------------+-------+
```
## Create a Database
Create a new database called parchment_dev. Also, view all databases to confirm creation.
```sql
CREATE DATABASE parchment_dev;
SHOW DATABASES;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| parchment_dev      |
| performance_schema |
| sys                |
+--------------------+
```
The new database `parchment_dev` is now ready for connection with Spring.
## Connect MySQL Database to Spring
The MySQL database must be connected to Spring via `application.properties` file by listing the database url, username, and password. The database URL is composed with the host, port, and database name as follows: `jdbc:mysql://host:port/database_name`.
```
spring.application.name=parchment-recipes-web
spring.datasource.url=jdbc:mysql://localhost:3306/parchment_dev
spring.datasource.username=user_name
spring.datasource.password=password
```
Also, provide the same details to the [IntelliJ database tool](https://www.jetbrains.com/help/idea/database-tool-window.html) to view the data, users, and other details that are helpful during development. Now, changes will be visible in the Database sidebar.
## Designing the SQL Model
The constraints of SQL requires the use of relationships to model even moderately complex data. Atlassian provides an example of modeling data for an album. In SQL, a tracks, albums and artists table models the data optimizing to eliminate duplicate values. View the [Atlassian article](https://www.atlassian.com/data/sql/joins) to learn more about primary keys and foriegn keys.



