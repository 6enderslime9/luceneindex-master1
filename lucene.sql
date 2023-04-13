# Host: 127.0.0.1  (Version 5.7.19)
# Date: 2023-03-23 15:21:56
# Generator: MySQL-Front 6.0  (Build 2.20)


#
# Structure for table "article"
#

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `classes` varchar(32) DEFAULT NULL,
  `href` varchar(512) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `text` text,
  `created` varchar(32) DEFAULT NULL,
  `sign` varchar(128) DEFAULT NULL,
  `requesttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "hibernate_sequence"
#

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
