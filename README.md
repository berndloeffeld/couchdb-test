couchdb-test
============

This software will 
- connect to couch db
- create a long polling connection
- print all received changes to the Console Logger
- and create 3 assets


It is a Maven project, so build it with
~~~bash
mvn package
~~~
to get an executable jar file and run it with
~~~bash
java -jar target/pollingtest-0.0.1-SNAPSHOT.jar
~~~