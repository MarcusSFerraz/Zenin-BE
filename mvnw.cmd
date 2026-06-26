@REM Maven Wrapper script for Windows
@echo off
setlocal

set MAVEN_WRAPPER_JAR=.mvn\wrapper\maven-wrapper.jar

if exist "%MAVEN_WRAPPER_JAR%" (
    java -jar "%MAVEN_WRAPPER_JAR%" %*
    exit /b %ERRORLEVEL%
)

mvn %*
exit /b %ERRORLEVEL%
