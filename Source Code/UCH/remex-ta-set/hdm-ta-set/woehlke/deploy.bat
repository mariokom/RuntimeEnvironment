cd..
cd..
::call mvn clean 
cd utility
::call mvn install
cd..
cd hdm-ta-set
cd woehlke

 call mvn package
set target=..\..\..\UCH\bin\resources\Woehlke

set fileName=woehlke-configuration-manager-1.1.jar
set projectName=woehlke-configuration-manager

md %target%\%projectName%\lib
copy %projectName%\target\lib %target%\%projectName%\lib
copy %projectName%\target\%fileName% %target%\%projectName%\%fileName%
copy %projectName%\%projectName%.rprop %target%\%projectName%\%projectName%.rprop



set fileName=woehlke-tdm-1.1.jar
set projectName="woehlke-tdm"

md %target%\%projectName%\lib
copy %projectName%\target\lib %target%\%projectName%\lib
copy %projectName%\target\%fileName% %target%\%projectName%\%fileName%
copy %projectName%\%projectName%.rprop %target%\%projectName%\%projectName%.rprop

set fileName=woehlke-ta-1.1.jar
set projectName="woehlke-ta"

md %target%\%projectName%\lib
copy %projectName%\target\lib %target%\%projectName%\lib
copy %projectName%\target\%fileName% %target%\%projectName%\%fileName%
copy %projectName%\%projectName%.rprop %target%\%projectName%\%projectName%.rprop
