@echo off
echo Initializing the database...

:GET_MYSQL_PATH
REM Ask the user for the MySQL installation path
set /p "MYSQL_INSTALL_PATH=Enter the path to your MySQL 8.4 installation directory (e.g., C:\Program Files\MySQL\MySQL Server 8.4): "

REM Check if mysql.exe exists in the specified path
if exist "%MYSQL_INSTALL_PATH%\bin\mysql.exe" (
    set "MYSQL_PATH=%MYSQL_INSTALL_PATH%\bin"
    goto MYSQL_PATH_OK
) else (
    echo MySQL was not found at the specified path. Please ensure the path is correct and includes the 'bin' directory.
    echo.
    goto GET_MYSQL_PATH
)

:MYSQL_PATH_OK

REM Ask for MySQL root password
set /p "MYSQL_ROOT_PASSWORD=Enter the root password for MySQL: "

REM Execute SQL commands to create the database and user
"%MYSQL_PATH%\mysql" -u root -p"%MYSQL_ROOT_PASSWORD%" -e "CREATE DATABASE IF NOT EXISTS da50;"
if %errorlevel% neq 0 (
    echo Error creating database. Check MySQL root password and try again.
    pause
    exit /b 1
)

"%MYSQL_PATH%\mysql" -u root -p"%MYSQL_ROOT_PASSWORD%" -e "CREATE USER IF NOT EXISTS 'hibernate_user'@'localhost' IDENTIFIED BY 'root';"
if %errorlevel% neq 0 (
    echo Error creating user. Check MySQL root password and try again.
    pause
    exit /b 1
)

"%MYSQL_PATH%\mysql" -u root -p"%MYSQL_ROOT_PASSWORD%" -e "GRANT ALL PRIVILEGES ON da50.* TO 'hibernate_user'@'localhost';"
if %errorlevel% neq 0 (
    echo Error granting privileges. Check MySQL root password and try again.
    pause
    exit /b 1
)

"%MYSQL_PATH%\mysql" -u root -p"%MYSQL_ROOT_PASSWORD%" -e "FLUSH PRIVILEGES;"
if %errorlevel% neq 0 (
    echo Error flushing privileges. Check MySQL root password and try again.
    pause
    exit /b 1
)

echo Database initialized successfully.

echo.
echo Do you want to create an admin account? (y/n)
set /p createAdmin=

if /i "%createAdmin%" equ "y" (
    echo Creating admin account...
    call create_admin.bat
) else (
    echo Admin account creation canceled.
)

pause