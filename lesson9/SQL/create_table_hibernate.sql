USE `otus_lesson9_hibernate`;

DROP TABLE IF EXISTS `Phone`;
DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `age` int(3) DEFAULT '0',  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Phone` (
  `id` int(11) NOT NULL AUTO_INCREMENT DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `user_id` int(11),
  PRIMARY KEY (`id`),
  KEY `fk_Phone_1_idx` (`user_id`),
  CONSTRAINT `fk_Phone_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
