# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.17)
# Database: AutoTestDB
# Generation Time: 2014-05-30 10:51:18 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table BatchCaseMap
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BatchCaseMap`;

CREATE TABLE `BatchCaseMap` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `batch_number` int(11) NOT NULL,
  `case_number` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `batch_case_map` (`batch_number`,`case_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `BatchCaseMap` WRITE;
/*!40000 ALTER TABLE `BatchCaseMap` DISABLE KEYS */;

INSERT INTO `BatchCaseMap` (`id`, `batch_number`, `case_number`)
VALUES
	(1,1,29),
	(2,1,30);

/*!40000 ALTER TABLE `BatchCaseMap` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BatchTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BatchTable`;

CREATE TABLE `BatchTable` (
  `batch_number` int(11) NOT NULL AUTO_INCREMENT,
  `batch_owner` char(50) DEFAULT NULL,
  `batch_state` int(11) DEFAULT NULL,
  PRIMARY KEY (`batch_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `BatchTable` WRITE;
/*!40000 ALTER TABLE `BatchTable` DISABLE KEYS */;

INSERT INTO `BatchTable` (`batch_number`, `batch_owner`, `batch_state`)
VALUES
	(1,'James',1);

/*!40000 ALTER TABLE `BatchTable` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table CaseArgumentTable
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseArgumentTable`;

CREATE TABLE `CaseArgumentTable` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `argument_name` char(100) NOT NULL DEFAULT '',
  `android_value` char(100) NOT NULL DEFAULT '',
  `ios_value` char(100) NOT NULL DEFAULT '',
  `argument_owner` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `CaseArgumentTable` WRITE;
/*!40000 ALTER TABLE `CaseArgumentTable` DISABLE KEYS */;

INSERT INTO `CaseArgumentTable` (`id`, `argument_name`, `android_value`, `ios_value`, `argument_owner`)
VALUES
	(1,'$CtripInfoBar','CtripInfoBar','CtripInfoBar','James'),
	(2,'$FlightHome','机票','cthome_flight','James'),
	(3,'$ProgressBar','ProgressBar','CTLoadingAnimationView','James'),
	(4,'$Retry','重试','重试','James'),
	(5,'$SorryLoadFail','抱歉','抱歉','James'),
	(6,'$Know','知道了','知道了','James'),
	(7,'$RWFromFlightInfo','国航CA1858','国航CA1858 330大','James'),
	(8,'$RWBackFlightInfo','国航CA1831','国航CA1831 33A大','James'),
	(9,'$DeletePerson','f3_button_person_list_select','icon del2','James'),
	(10,'$AddPerson','f3_boarding_person','btn flightadd','James'),
	(11,'$ContactPhone','EditText','UITextField','James'),
	(12,'$ToastView','toast_message','CTToastTipView','James'),
	(13,'$CVV2','签名栏末尾最后3位','签名末尾处最后3位','James'),
	(14,'$RepeatOrder','继续预订','继续预订','James');

/*!40000 ALTER TABLE `CaseArgumentTable` ENABLE KEYS */;
UNLOCK TABLES;


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
	(29,'国内往返','James');

/*!40000 ALTER TABLE `CaseListTable` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table CaseResult
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CaseResult`;

CREATE TABLE `CaseResult` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `batch_number` int(11) DEFAULT NULL,
  `case_number` int(11) DEFAULT '0',
  `device_name` varchar(200) DEFAULT '',
  `case_excute_state` int(11) DEFAULT NULL,
  `case_result_path` varchar(500) NOT NULL DEFAULT '',
  `case_start_time` datetime DEFAULT NULL,
  `case_end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `result_define` (`batch_number`,`case_number`,`device_name`,`case_start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `CaseResult` WRITE;
/*!40000 ALTER TABLE `CaseResult` DISABLE KEYS */;

INSERT INTO `CaseResult` (`id`, `batch_number`, `case_number`, `device_name`, `case_excute_state`, `case_result_path`, `case_start_time`, `case_end_time`)
VALUES
	(17,1,29,'iPhone Simulator',2,'/Users/yrguo/work/UIAutoServer/TestResult/国内往返/iPhone Simulator/2014-05-30-18-44-12','2014-05-30 18:44:12','2014-05-30 18:45:16'),
	(19,1,29,'iPhone Simulator',2,'/Users/yrguo/work/UIAutoServer/TestResult/国内往返/iPhone Simulator/2014-05-30-18-45-16','2014-05-30 18:45:16','2014-05-30 18:46:13'),
	(21,1,29,'iPhone Simulator',2,'/Users/yrguo/work/UIAutoServer/TestResult/国内往返/iPhone Simulator/2014-05-30-18-46-13','2014-05-30 18:46:13','2014-05-30 18:47:10'),
	(23,1,29,'iPhone Simulator',3,'/Users/yrguo/work/UIAutoServer/TestResult/国内往返/iPhone Simulator/2014-05-30-18-47-10','2014-05-30 18:47:10','2014-05-30 18:47:21'),
	(26,1,29,'iPhone Simulator',2,'/Users/yrguo/work/UIAutoServer/TestResult/国内往返/iPhone Simulator/2014-05-30-18-47-47','2014-05-30 18:47:47','2014-05-30 18:48:54'),
	(28,1,29,'iPhone Simulator',2,'/Users/yrguo/work/UIAutoServer/TestResult/国内往返/iPhone Simulator/2014-05-30-18-48-54','2014-05-30 18:48:54','2014-05-30 18:50:25'),
	(29,1,29,'iPhone Simulator',1,'/Users/yrguo/work/UIAutoServer/TestResult/国内往返/iPhone Simulator/2014-05-30-18-50-25','2014-05-30 18:50:25','2014-05-30 18:50:25');

/*!40000 ALTER TABLE `CaseResult` ENABLE KEYS */;
UNLOCK TABLES;


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
	(32,29,'start','wait(5);DeviceInfo();','see($FlightHome,goto:step2);goto:error','goto:error;',65535),
	(33,29,'step2','find($FlightHome);click()','see(查询,goto:step3);goto:error','goto:error;',65535),
	(34,29,'step3','screenshot(查询页.jpg);find(往返);click();find(查询);click();','seeView($ProgressBar,goto:waitProcess);see($RWFromFlightInfo,goto:step5);see($Retry,goto:step4);goto:error','goto:error;',65535),
	(35,29,'step4','find($Retry);click();','seeView($ProgressBar,goto:waitProcess);see($RWFromFlightInfo,goto:step5);see($Retry,goto:step4);goto:error','goto:error;',3),
	(36,29,'step5','find($RWFromFlightInfo);click();','seeView($ProgressBar,goto:waitProcess);see(选定,goto:step7);see($Know,step6);goto:error','goto:error;',65535),
	(37,29,'step6','find($Know);click();','see(,goto:step5);goto:error','goto:error;',3),
	(38,29,'step7','find(选定);click();','seeView($ProgressBar,goto:waitProcess);see($RWBackFlightInfo,goto:step10);see($Retry,goto:step9);goto:error','goto:error;',65535),
	(39,29,'step9','find($Retry);click();','seeView($ProgressBar,goto:waitProcess);see($RWBackFlightInfo,goto:step5);see($Retry,goto:step9);goto:error','goto:error;',3),
	(40,29,'step10','find($RWBackFlightInfo);click();','seeView($ProgressBar,goto:waitProcess);see(预订,goto:step12);see($Know,step11);goto:error','goto:error;',65535),
	(41,29,'step11','find($Know);click();','see(,goto:step10);goto:error','goto:error;',3),
	(42,29,'step12','find(预订);click();','seeView($ProgressBar,goto:waitProcess);see($Know,goto:step13);see($DeletePerson,goto:step14);goto:step15','goto:error;',65535),
	(43,29,'step13','find($Know);click();','see(,goto:step12);goto:error','goto:error;',3),
	(44,29,'step14_Android','find($DeletePerson);click();','see($DeletePerson,goto:step14);goto:step15','goto:step15;',65535),
	(45,29,'step14_IOS','find($DeletePerson);click();find(删除);click();','see($DeletePerson,goto:step14);goto:step15','goto:step15;',65535),
	(46,29,'step15','find($AddPerson);click();find(小辛xiao/xin);click();find(完成);click();','see(联系手机,goto:step16);goto:error','goto:error;',65535),
	(47,29,'step16','findView($ContactPhone);presskey(13518336231);','see(下一步,goto:step17);goto:error','goto:error;',65535),
	(48,29,'step17','find(下一步);click();','seeView($ProgressBar,goto:waitProcess);see(支付方式,goto:step18);see($RepeatOrder,goto:step19);see($Know,goto:error);goto:error','goto:error;',65535),
	(49,29,'step18','find(常用卡支付);click();find(民生银行-信用卡);click();find(请输入卡号后四位);presskey(0008);find($CVV2);presskey(321);find(支付);click();','seeView($ProgressBar,goto:waitProcess);seeView($ToastView,goto:step18);see(订单结果,goto:success);goto:error','goto:error;',3),
	(50,29,'step19','find($RepeatOrder);click();','see(,goto:step18);goto:step18','goto:step18',65535),
	(51,29,'waitProcess','findView($ProgressBar);WaitToDisappear()','','',65535),
	(52,29,'success','screenshot(success.jpg);finish()','','',65535),
	(53,29,'error','screenshot(error.jpg);finish()','','',65535);

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_name` (`device_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `DeviceInfoTable` WRITE;
/*!40000 ALTER TABLE `DeviceInfoTable` DISABLE KEYS */;

INSERT INTO `DeviceInfoTable` (`id`, `device_name`, `device_os`, `device_height`, `device_width`, `device_version`)
VALUES
	(27,'iPhone Simulator','IOS',0,0,'7.0.3');

/*!40000 ALTER TABLE `DeviceInfoTable` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
