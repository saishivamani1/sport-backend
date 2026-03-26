@echo off
echo Setting Java 21 environment...
set "JAVA_HOME=C:\Users\saish\.antigravity\extensions\redhat.java-1.53.0-win32-x64\jre\21.0.10-win32-x86_64"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Using Java version:
java -version
echo.
echo Compiling and running the application...
mvn clean spring-boot:run
pause
