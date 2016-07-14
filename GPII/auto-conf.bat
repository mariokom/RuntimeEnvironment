cd ..

SET pathSingle=%CD%

SET pathDouble=%pathSingle:\=\\%

cd GPII

powershell -Command "(gc win32.json) -replace '<rootFolderPath>', '%pathDouble%' | Out-File win32.json"