@echo off

set STOP_JAR=L:\workspace\_maven_repository_\fr\mla\STop\1.0-SNAPSHOT\STop-1.0-SNAPSHOT.jar
set STOP_MAIN_CLASS=fr.mla.stop.STop
set ODS_PATH=L:\workspace\STop\ODS5.txt

::set INPUT=L:\workspace\STop\inEga.txt
::set INPUT=L:\workspace\STop\inCathy.txt
set INPUT=L:\workspace\STop\in.txt

::set TYPE=SCRABBLE
set TYPE=WORDFEUD

%JAVA_HOME%\bin\java -cp %STOP_JAR% %STOP_MAIN_CLASS% %ODS_PATH% %INPUT% %TYPE%