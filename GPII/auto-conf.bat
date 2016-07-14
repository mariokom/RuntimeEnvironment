SET pathSingle=%CD%

SET pathDouble=%pathSingle:\=\\%

powershell -Command "(gc win32.json) -replace '<rootFolderPath>', '%pathDouble%' | Out-File win32.json"