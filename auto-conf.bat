@echo.
@echo.
@echo.

@echo *** Copying MyUI to UCH server...

@REM 'E' parameter to copy even the empty folders, /Y parameter to overwrite
xcopy .\MyUI .\UCH\bin\Webclient\MyUI\ /E /Y

@REM check if the copy operation was succesfull
if %errorlevel% equ 0 GOTO :OKCOPY
 @echo.
 @echo ***** Copy operation failed (xcopy errorlevel: %errorlevel%)
 GOTO :FINISH
:OKCOPY
@echo.
@echo *** MyUI copied successfully.

@echo.
@echo.
@echo.
@echo.
@echo.
@echo.


@echo *** Modifing 'GPII/win32.json' file...

SET pathSingle=%CD%

SET pathDouble=%pathSingle:\=\\%

cd GPII

powershell -Command "(gc win32.json) -replace '<rootFolderPath>', '%pathDouble%' | Out-File win32.json -encoding ASCII"

@REM check if the copy operation was succesfull
if %errorlevel% equ 0 GOTO :OKWINN32
 @echo.
 @echo ***** 'GPII/win32.json' modification failed (xcopy errorlevel: %errorlevel%)
 GOTO :FINISH
:OKWINN32
@echo.
@echo *** 'GPII/win32.json' was modified successfully.

@echo.
@echo.
@echo.
@echo.
@echo.
@echo.


set /p destination=*** Enter path to GPII installation folder: 

@echo.
@echo.
@echo.
@echo.
@echo.
@echo.

@echo *** Copying GPII configuration files...

if exist "%dir%\New folder 1" GOTO :OKWINN32
 @echo.
 @echo ***** Copy operation failed: Unable to find the folder given as a parameter
 GOTO :FINISH

:OKWINN32
xcopy /f ".\installedSolutions.json" "%destination%\node_modules\universal\testData\deviceReporter\" /y
xcopy /f ".\ISO24751-flat.json" "%destination%\node_modules\universal\testData\ontologies\" /y
xcopy /f ".\mr_moroz.json" "%destination%\node_modules\universal\testData\preferences\" /y
xcopy /f ".\ms_moroz.json" "%destination%\node_modules\universal\testData\preferences\" /y
xcopy /f ".\nicholas.json" "%destination%\node_modules\universal\testData\preferences\" /y
xcopy /f ".\win32.json" "%destination%\node_modules\universal\testData\preferences\solutions" /y
@echo.
@echo *** GPII conficuration files were copied successfully.

@echo.
@echo.
@echo.
@echo.
@echo.
@echo.

@echo ***** Auto-configuration completed successfully.

:FINISH

@echo.
@echo.
@echo.
@echo.
@echo.
@echo.

pause



