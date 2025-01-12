@echo off
echo Creating admin account...

REM Prompt for the MySQL installation path if not already defined
if not defined MYSQL_INSTALL_PATH (
    set /p "MYSQL_INSTALL_PATH=Enter the path to your MySQL 8.4 installation directory (e.g., C:\Program Files\MySQL\MySQL Server 8.4): "
)

REM Check if mysql.exe exists in the specified path
if exist "%MYSQL_INSTALL_PATH%\bin\mysql.exe" (
    set "MYSQL_PATH=%MYSQL_INSTALL_PATH%\bin"
) else (
    echo MySQL was not found at the specified path. Please ensure the path is correct and includes the 'bin' directory.
    pause
    exit /b 1
)

REM Prompt for the MySQL root password if not already defined
if not defined MYSQL_ROOT_PASSWORD (
    set /p "MYSQL_ROOT_PASSWORD=Enter the root password for MySQL: "
)

REM Verify the connection to MySQL
"%MYSQL_PATH%\mysql" -u root -p"%MYSQL_ROOT_PASSWORD%" -e "SELECT 1;"
if %errorlevel% neq 0 (
    echo Error connecting to MySQL. Check MySQL root password and try again.
    pause
    exit /b 1
)

REM Execute CreateAdminUser to create the admin account
java -cp "target/classes;target/lib/*" com.projet.da50.projet_da50.CreateAdminUser

pause