
call mvn install
set target=..\..\..\uch\bin\resources\woehlke
set file=ta-framework-1.0.jar


copy target\%file% %target%\woehlke-configuration-manager\lib\%file%
copy target\%file% %target%\woehlke-ta\lib\%file%
copy target\%file% %target%\woehlke-tdm\lib\%file%
