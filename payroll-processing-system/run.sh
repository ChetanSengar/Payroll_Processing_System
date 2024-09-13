#!/bin/bash
echo "Current directory: $(pwd)"
echo "Listing JAR files in target directory:"
ls target/*.jar
JAVA_OPTS="-Xms1024m -Xmx1024m -Djava.net.preferIPv4Stack=true -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 -XX:+HeapDumpOnOutOfMemoryError -Xrunjdwp:transport=dt_socket,address=0.0.0.0:8081,server=y,suspend=n"
mvn clean install
if [ $? -eq 0 ]; then
    java $JAVA_OPTS -jar target/payroll-processing-system-0.0.1-SNAPSHOT.jar
else
    echo "Build failed, not running the application."
fi
