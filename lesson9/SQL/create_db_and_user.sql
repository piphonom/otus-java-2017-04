CREATE DATABASE IF NOT EXISTS `otus_lesson9`;
CREATE USER 'piphonom'@'localhost' IDENTIFIED BY 'monohpip';
GRANT SELECT, INSERT, UPDATE, EXECUTE, SHOW VIEW, LOCK TABLES ON `otus_%`.* to 'piphonom'@'%';
