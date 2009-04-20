@echo off
rem HTMLParser Library $Name: v1_6 $ - A java-based parser for HTML
rem http://sourceforge.org/projects/htmlparser
rem Copyright (C) 2005 Derrick Oswald
rem
rem Revision Control Information
rem
rem $Source: /cvsroot/htmlparser/htmlparser/bin/stringextractor.cmd,v $
rem $Author: derrickoswald $
rem $Date: 2006/04/17 13:51:19 $
rem $Revision: 1.2 $
rem
rem This library is free software; you can redistribute it and/or
rem modify it under the terms of the GNU Lesser General Public
rem License as published by the Free Software Foundation; either
rem version 2.1 of the License, or (at your option) any later version.
rem
rem This library is distributed in the hope that it will be useful,
rem but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
rem Lesser General Public License for more details.
rem
rem You should have received a copy of the GNU Lesser General Public
rem License along with this library; if not, write to the Free Software
rem Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
rem
setlocal enableextensions
if errorlevel 1 goto no_extensions_error
for %%i in ("%0") do set cmd_path=%%~dpi
for /D %%i in ("%cmd_path%..\lib\") do set lib_path=%%~dpi
if not exist "%lib_path%htmlparser.jar" goto no_jar_error
for %%i in (java.exe) do set java_executable=%%~$PATH:i
if "%java_executable%"=="" goto no_java_error
@echo on
%java_executable% -classpath "%lib_path%htmlparser.jar" org.htmlparser.parserapplications.StringExtractor %1 %2
@echo off
goto end
:no_extensions_error
echo Unable to use CMD extensions
goto end
:no_jar_error
echo Unable to find htmlparser.jar
goto end
:no_java_error
echo Unable to find java.exe
goto end
:end
