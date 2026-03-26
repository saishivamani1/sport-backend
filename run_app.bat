@echo off
set JAVA_HOME=%CD%\jdk17\jdk-17.0.10+7
set PATH=%JAVA_HOME%\bin;%PATH%
mvn spring-boot:run
