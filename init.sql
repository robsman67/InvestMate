CREATE DATABASE da50;
CREATE USER 'hibernate_user'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON da50.* TO 'hibernate_user'@'localhost';
FLUSH PRIVILEGES;
