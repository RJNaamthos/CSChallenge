# CSChallenge

This Spring boot Command Line Project takes path to a text log file as an input argument and parse it's contents. Also flag events if takes longer than 4ms. Events are stored in HSQL DB.

Steps to run:
cd PROJECT_DIR
mvn clean install -Dmaven.test.skip=true
java -jar target/EventLogApplication-0.0.1-SNAPSHOT.jar {input-file-path}