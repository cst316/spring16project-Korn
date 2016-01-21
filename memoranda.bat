@echo off

set PATHCOPY=%PATH%
path %PATH%;lib/win32

if "%1"=="debug" goto debug

start javaw -cp build/memoranda.jar;lib/xercesImpl.jar;lib/xmlParserAPIs.jar;lib/xom-1.0.jar;lib/nekohtml.jar;lib/nekohtmlXni.jar net.sf.memoranda.Start %1
goto end

:debug
java %2 -cp build/memoranda.jar;lib/xercesImpl.jar;lib/xmlParserAPIs.jar;lib/xom-1.0.jar;lib/nekohtml.jar;lib/nekohtmlXni.jar net.sf.memoranda.Start %3
:end
path %PATHCOPY%
set PATHCOPY=
