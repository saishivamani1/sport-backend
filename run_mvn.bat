@echo off
set JAVA_HOME=%CD%\jdk17\jdk-17.0.10+7
set PATH=%JAVA_HOME%\bin;%PATH%
java -version
mvn clean test > test.log 2>&1
