call mvn install:install-file -Dfile=uchinterfaces.jar -DgroupId=org.openurc -DartifactId=uchinterfaces -Dpackaging=jar -Dversion=1.0 -DgeneratePom=true


call mvn install:install-file -Dfile=uchutil.jar -DgroupId=org.openurc -DartifactId=uchutil -Dpackaging=jar -Dversion=1.0 -DgeneratePom=true



call mvn install:install-file -Dfile=huelocalsdk.jar -DgroupId=hue -DartifactId=huelocalsdk -Dpackaging=jar -Dversion=1.0 -DgeneratePom=true

call mvn install:install-file -Dfile=huesdkresources.jar -DgroupId=hue -DartifactId=huesdkresources -Dpackaging=jar -Dversion=1.0 -DgeneratePom=true