@echo UCH wird gestartet
@echo Log Meldungen werden nach normal.log und error.log geschrieben.
@echo Tippen Sie "stop" um den UCH zu stoppen.
 cd bin  

java -jar UCH.jar >..\normal.log 2>..\error.log
cd..