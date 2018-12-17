
## H2 Database
H2 is a Java database.

### Download
```sh
cd ~/Downloads/
curl -L -O http://repo2.maven.org/maven2/com/h2database/h2/1.4.197/h2-1.4.197.jar
```

### Install
```sh
mkdir ~/tailwind-tutorials
mv ~/Downloads/h2-1.4.197.jar ~/tailwind-tutorials
```

### Run
```sh
java -jar ~/tailwind-tutorials/h2-1.4.197.jar
```

## Set environment variables
The environment variables defined below are necessary for the Spring Boot application deployed to the Tomcat server to be able to connect to the H2 database that was started in the previous section.
```sh
export SPRING_DATASOURCE_URL=jdbc:h2:tcp://localhost/~/test
export SPRING_DATASOURCE_USERNAME=sa
```

## Tomcat

### Download  

```sh
cd ~/Downloads/
curl -L -O https://www-eu.apache.org/dist/tomcat/tomcat-8/v8.5.35/bin/apache-tomcat-8.5.35.zip
```

### Install

```sh
unzip apache-tomcat-8.5.35.zip -d ~/tailwind-tutorials
chmod +x ~/tailwind-tutorials/apache-tomcat-8.5.35/bin/*.sh
```

### Run
```sh
~/tailwind-tutorials/apache-tomcat-8.5.35/bin/startup.sh
```

## Database Migrations

Many projects rely on manually applied sql scripts.  Soon many questions arise:

* What state is the database in on this server?
* Has this script already been applied or not?
* Has the quick fix in production been applied in test afterwards?
* How do you set up a new database instance?

More often than not the answer to these questions is: We don't know.

Database migrations are a great way to regain control of this situation.

They allow you to:

* Recreate a database from scratch
* Make it clear at all times what state a database is in
* Migrate in a deterministic way from your current version of the database to a newer one

*(text adapted from the flyway [getting started guide](https://flywaydb.org/getstarted/why))*

## Tailwind

Tailwind demonstrates how Flyway from [Boxfuse](https://boxfuse.com) can be used to version a database.

### Download
```sh
cd ~/Downloads/
curl -L -O https://bitbucket.org/berrycloud/tailwind/downloads/tailwind-1.0.0.zip
unzip -o tailwind-1.0.0.zip

curl -L -O https://bitbucket.org/berrycloud/tailwind/downloads/tailwind-1.1.0.zip
unzip -o tailwind-1.1.0.zip

curl -L -O https://bitbucket.org/berrycloud/tailwind/downloads/tailwind-1.2.0.zip
unzip -o tailwind-1.2.0.zip

curl -L -O https://bitbucket.org/berrycloud/tailwind/downloads/tailwind-1.3.0.zip
unzip -o tailwind-1.3.0.zip

curl -L -O https://bitbucket.org/berrycloud/tailwind/downloads/tailwind-1.3.1.zip
unzip -o tailwind-1.3.1.zip
```

## Step 1. Inspecting empty schema
After the H2 database is started it creates an empty database schema, which does not contain any user-defined tables. This empty schema can be investigated using the H2 graphic Console that opens automatically in the default browser when the H2 database is started. Click the "Connect" button and browse the currently defined database objects on the left panel of the page.

## Step 2. Deploying version 1.0.0
Please see the [release notes](https://berrycloud.atlassian.net/secure/ReleaseNote.jspa?projectId=11703&version=11100) for the list of features implemented in this version.  
The database migration script can be found [here](https://bitbucket.org/berrycloud/tailwind/src/master/tailwind/src/main/resources/db/migration/h2/V1.0.0__Add_Members.sql).

### Deploy the application
```sh
cp ~/Downloads/tailwind-1.0.0.war ~/tailwind-tutorials/apache-tomcat-8.5.35/webapps/tailwind.war
```

### Inspect database schema changes
After Tailwind version 1.0.0 has been deployed, the "MEMBER" table should be present in the database. To see these changes, press the "refresh" button in the H2 Console.

### Insert sample data
You can simulate usage of the application by inserting the following sample data:
```sql
insert into member (id, first_name, last_name) values
    (1, 'Shelley', 'Surfleet'),
    (2, 'Perceval', 'Reame'),
    (3, 'Dorey', 'Bowcock'),
    (4, 'Darin', 'Mathiasen'),
    (5, 'Aleta', 'Sirrell'),
    (6, 'Philis', 'Dumberrill'),
    (7, 'Cheston', 'Tarbett'),
    (8, 'Carlyle', 'Shoreson'),
    (9, 'Carlynne', 'Flay'),
    (10, 'Cherilyn', 'Tudball');
```

## Step 3. Deploying version 1.1.0
Please see the [release notes](https://berrycloud.atlassian.net/secure/ReleaseNote.jspa?projectId=11703&version=11101) for the list of features implemented in this version.  
The database migration script can be found [here](https://bitbucket.org/berrycloud/tailwind/src/master/tailwind/src/main/resources/db/migration/h2/V1.1.0__Add_Plans.sql).

### Deploy the application
```sh
cp ~/Downloads/tailwind-1.1.0.war ~/tailwind-tutorials/apache-tomcat-8.5.35/webapps/tailwind.war
```

### Inspect database schema changes
After Tailwind version 1.1.0 has been deployed, the new "PLAN" table should be present in the database, as well as the new mapping table "MEMBER_PLAN". To see these changes, press the "refresh" button in the H2 Console.

### Insert sample data
Populate the newly created tables with these data:
```sql
insert into plan (id, name, organization) values
    (1, 'Wallaby', 'Jamia'),
    (2, 'Squirrel', 'Tagchat'),
    (3, 'Lizard', 'Skaboo'),
    (4, 'Guerza', 'Twitterbeat'),
    (5, 'Onager', 'Skyble'),
    (6, 'Hedgehog', 'Tagchat');

insert into member_plan (member_id, plan_id) values
    (1, 1),
    (10, 5),
    (10, 3),
    (9, 3),
    (4, 5),
    (2, 2),
    (10, 4),
    (3, 4),
    (3, 1),
    (5, 1);
```

## Step 4. Deploying version 1.2.0
Please see the [release notes](https://berrycloud.atlassian.net/secure/ReleaseNote.jspa?projectId=11703&version=11102) for the list of features implemented in this version.  
The database migration script can be found [here](https://bitbucket.org/berrycloud/tailwind/src/master/tailwind/src/main/resources/db/migration/h2/V1.2.0__Add_Member_Birth_And_Retirement_Dates.sql).

### Deploy the application
```sh
cp ~/Downloads/tailwind-1.2.0.war ~/tailwind-tutorials/apache-tomcat-8.5.35/webapps/tailwind.war
```

### Inspect database schema changes
After the 1.2.0 version of the application has been deployed, the "MEMBER" table should have four new columns: 'date_of_birth', 'retirement', 'created' and 'last_modified'. To see these changes, press the "refresh" button in the H2 Console. Notice how the existing data has been preserved during the migration.

### Create User

```sh
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"firstName":"Muffin","lastName":"Honywill","dateOfBirth":"2010-05-07"}' \
  http://localhost:8080/tailwind/members

curl --header "Content-Type: application/json" \
  --request PATCH \
  --data '{"lastName":"Nuffin"}' \
  http://localhost:8080/tailwind/members/11
```

Notice how after POSTing a new member the 'created' and 'last_modified' dates are populated automatically. Also, after PATCHing the user with a new last name, the 'last_modified' date changes.

## Step 5. Deploying version 1.3.0
Please see the [release notes](https://berrycloud.atlassian.net/secure/ReleaseNote.jspa?projectId=11703&version=11103) for the list of features implemented in this version.  
The database migration script can be found [here](https://bitbucket.org/berrycloud/tailwind/src/master/tailwind/src/main/resources/db/migration/h2/V1.3.0__Add_Organization.sql).

### Deploy the application
```sh
cp ~/Downloads/tailwind-1.3.0.war ~/tailwind-tutorials/apache-tomcat-8.5.35/webapps/tailwind.war
```

### Inspect database schema changes
After the 1.3.0 version of the application has been deployed, the new "ORGANIZATION" table should be present in the database. Moreover, this new table should be populated by records constructed from the unique values of the 'name' column of the 'PLAN' table. To see these changes, press the "refresh" button in the H2 Console.

"PLAN" table before the migration:

![picture](tutorials/images/plan_prior_to_1_3_0.png)  

"PLAN" table after the migration:

![picture](tutorials/images/plan_after_to_1_3_0.png)  

"ORGANIZATION" table:

![picture](tutorials/images/organization.png)  


## Step 6. Deploying version 1.3.1
Please see the [release notes](https://berrycloud.atlassian.net/secure/ReleaseNote.jspa?projectId=11703&version=11104) for the list of features implemented in this version.  
The database migration script can be found [here](https://bitbucket.org/berrycloud/tailwind/src/master/tailwind/src/main/resources/db/migration/h2/V1.3.1__Populate_Organization_Plan_Tables.sql).

### Deploy the application
```sh
cp ~/Downloads/tailwind-1.3.1.war ~/tailwind-tutorials/apache-tomcat-8.5.35/webapps/tailwind.war
```

### Inspect database schema changes
After the 1.3.1 version of the application has been deployed, three new records should be added to the "ORGANIZATION" table. To see these changes, press the "refresh" button in the H2 Console.

## Tidying up
```sh
~/tailwind-tutorials/apache-tomcat-8.5.35/bin/shutdown.sh
rm ~/test.mv.db
```
