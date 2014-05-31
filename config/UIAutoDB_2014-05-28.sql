# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.17)
# Database: AutoTestDB
# Generation Time: 2014-05-29 10:13:27 +0000
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
  `batch_number` int(11) NOT NULL AUTO_INCREMENT,
  `case_list` varchar(1000) DEFAULT '',
  `batch_owner` int(11) DEFAULT NULL,
  `batch_state` int(11) DEFAULT NULL,
  PRIMARY KEY (`batch_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CaseArgumentTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseArgumentTable`;

CREATE TABLE `CaseArgumentTable` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `argument_name` char(100) NOT NULL DEFAULT '',
  `android_value` char(100) NOT NULL DEFAULT '',
  `ios_value` char(100) NOT NULL DEFAULT '',
  `argument_owner` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CaseListTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseListTable`;

CREATE TABLE `CaseListTable` (
  `case_number` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `case_name` char(100) DEFAULT '',
  `case_owner` char(50) DEFAULT 'UITester',
  PRIMARY KEY (`case_number`),
  UNIQUE KEY `case_define` (`case_name`,`case_owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `CaseListTable` WRITE;
/*!40000 ALTER TABLE `CaseListTable` DISABLE KEYS */;

INSERT INTO `CaseListTable` (`case_number`, `case_name`, `case_owner`)
VALUES
	(28,'国','James');

/*!40000 ALTER TABLE `CaseListTable` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table CaseResult
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseResult`;

CREATE TABLE `CaseResult` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `batch_number` int(11) DEFAULT NULL,
  `case_number` int(11) DEFAULT '0',
  `case_result` tinyint(1) DEFAULT '0',
  `case_result_path` varchar(500) NOT NULL DEFAULT '',
  `case_start_time` time DEFAULT NULL,
  `case_end_time` time DEFAULT NULL,
  `device_name` varchar(200) NOT NULL DEFAULT '',
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
  `step_limit` int(11) unsigned DEFAULT '65535',
  PRIMARY KEY (`id`),
  UNIQUE KEY `case_number` (`case_number`,`step_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `CaseStepListTable` WRITE;
/*!40000 ALTER TABLE `CaseStepListTable` DISABLE KEYS */;

INSERT INTO `CaseStepListTable` (`id`, `case_number`, `step_name`, `step_action`, `step_asset`, `step_error`, `step_limit`)
VALUES
	(27,28,'step1','find(上海);','see','goto',50),
	(28,28,'step2','find($Flight);','see','goto',50),
	(29,28,'step3','find();','see','goto',50),
	(30,28,'step4','find();','see','goto',50),
	(31,28,'step5','find();','see','goto',50);

/*!40000 ALTER TABLE `CaseStepListTable` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table DeviceInfoTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `DeviceInfoTable`;

CREATE TABLE `DeviceInfoTable` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `device_name` varchar(200) DEFAULT NULL,
  `device_os` char(10) DEFAULT NULL,
  `device_height` int(11) DEFAULT NULL,
  `device_width` int(11) DEFAULT NULL,
  `device_version` char(25) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
