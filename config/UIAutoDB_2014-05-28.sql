# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.17)
# Database: UIAutoDB
# Generation Time: 2014-05-28 15:58:10 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table BatchTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BatchTable`;

CREATE TABLE `BatchTable` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `bitch_number` int(11) DEFAULT '0',
  `case_list` varchar(1000) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CaseArgumentTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseArgumentTable`;

CREATE TABLE `CaseArgumentTable` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `argument_name` char(100) NOT NULL DEFAULT '',
  `android_value` char(100) NOT NULL DEFAULT '',
  `ios_value` char(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CaseInfoTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseInfoTable`;

CREATE TABLE `CaseInfoTable` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `case_number` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `case_number` (`case_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CaseListTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseListTable`;

CREATE TABLE `CaseListTable` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `case_number` int(11) DEFAULT '0',
  `case_name` text,
  `case_owner` char(50) NOT NULL DEFAULT 'UITester',
  PRIMARY KEY (`id`),
  UNIQUE KEY `case_number` (`case_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CaseResult
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseResult`;

CREATE TABLE `CaseResult` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `batch_number` int(11) DEFAULT NULL,
  `case_number` int(11) DEFAULT '0',
  `case_result` tinyint(1) DEFAULT '0',
  `case_result_path` varchar(11) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CaseStepListTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseStepListTable`;

CREATE TABLE `CaseStepListTable` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `case_number` int(11) DEFAULT '0',
  `step_name` char(25) DEFAULT '',
  `step_action` varchar(500) DEFAULT '',
  `step_asset` varchar(500) DEFAULT '',
  `step_error` char(50) DEFAULT '',
  `step_limit` int(11) DEFAULT '65535',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
