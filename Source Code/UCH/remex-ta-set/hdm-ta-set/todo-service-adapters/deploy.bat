call mvn package -DskipTests
set target=..\..\..\UCH\dev\bin\resources

set fileName=todo-service-tdm-0.0.1-SNAPSHOT.jar
set projectName="todo-service-tdm"
set rpropName=todo-service-tdm.rprop

copy %projectName%\target\lib %target%\%projectName%\lib
copy %projectName%\target\%fileName% %target%\%projectName%\%fileName%
copy %projectName%\%rpropName% %target%\%projectName%\%rpropName%

set fileName=todo-service-ta-0.0.1-SNAPSHOT.jar
set projectName="todo-service-ta"
set rpropName=todo-service-ta.rprop

copy %projectName%\target\lib %target%\%projectName%\lib
copy %projectName%\target\%fileName% %target%\%projectName%\%fileName%
copy %projectName%\%rpropName% %target%\%projectName%\%rpropName%