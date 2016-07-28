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

if exist "%destination%" GOTO :OKWINN32
 @echo.
 @echo ***** Copy operation failed: Unable to find the folder given as a parameter
GOTO :FINISH

:OKWINN32


xcopy ".\installedSolutions.json" "%destination%\node_modules\universal\testData\deviceReporter\" /y /f
SET copyerror1=">>> Copy successful"
if %errorlevel% equ 0 GOTO :C1
SET copyerror1 = Failed to copy 'installedSolutions' file. Please manually copy-paste it from '%CD%' to '%destination%\node_modules\universal\testData\deviceReporter\'
:C1


xcopy ".\ISO24751-flat.json" "%destination%\node_modules\universal\testData\ontologies\" /y /f
SET copyerror2=">>> Copy successful"
if %errorlevel% equ 0 GOTO :C2
SET copyerror2 = Failed to copy 'ISO24751-flat' file. Please manually copy-paste it from '%CD%' to '%destination%\node_modules\universal\testData\ontologies\'
:C2


xcopy ".\mr_moroz.json" "%destination%\node_modules\universal\testData\preferences\" /y /f
SET copyerror3=">>> Copy successful"
if %errorlevel% equ 0 GOTO :C3
SET copyerror3 = Failed to copy 'mr_moroz' file. Please manually copy-paste it from '%CD%' to '%destination%\node_modules\universal\testData\preferences\'
:C3


xcopy ".\ms_moroz.json" "%destination%\node_modules\universal\testData\preferences\" /y /f
SET copyerror4=">>> Copy successful"
if %errorlevel% equ 0 GOTO :C4
SET copyerror4 = Failed to copy 'ms_moroz' file. Please manually copy-paste it from '%CD%' to '%destination%\node_modules\universal\testData\preferences\'
:C4


xcopy ".\nicholas.json" "%destination%\node_modules\universal\testData\preferences\" /y /f
SET copyerror5=">>> Copy successful"
if %errorlevel% equ 0 GOTO :C5
SET copyerror5 = Failed to copy 'nicholas' file. Please manually copy-paste it from '%CD%' to '%destination%\node_modules\universal\testData\preferences\'
:C5

xcopy ".\win32.json" "%destination%\node_modules\universal\testData\solutions\" /y /f
SET copyerror6=">>> Copy successful"
if %errorlevel% equ 0 GOTO :C6
SET copyerror6 = Failed to copy 'win32' file. Please manually copy-paste it from '%CD%' to '%destination%\node_modules\universal\testData\solutions\'
:C6


@echo.
if NOT %copyerror1%==">>> Copy successful" goto :cond
if NOT %copyerror2%==">>> Copy successful" goto :cond
if NOT %copyerror3%==">>> Copy successful" goto :cond
if NOT %copyerror4%==">>> Copy successful" goto :cond
if NOT %copyerror5%==">>> Copy successful" goto :cond
if NOT %copyerror6%==">>> Copy successful" goto :cond
goto :FINISH_M
:cond
@echo.
@echo.
@echo *** Unable to copy some GPII configuration files. Please see the guidelines below and try to manually copy-paste them:
@echo.
@echo %copyerror1%
@echo %copyerror2%
@echo %copyerror3%
@echo %copyerror4%
@echo %copyerror5%
@echo %copyerror6%
@echo.
@echo The auto-configuration execution will be considered successful, if the above guilines are completed.

goto :FINISH


@echo *** GPII configuration files were copied successfully.

:FINISH_M

@echo.
@echo.
@echo.
@echo.
@echo.
@echo.

@echo *** Auto-configuration completed successfully.

:FINISH

@echo.
@echo.
@echo.
@echo.

pause



