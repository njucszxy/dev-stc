INSERT INTO `tbl_sys_consigns` ( `CONSIGNATION`,`PROCESS_INSTANCE_ID`, `ID`, `CODE`, `NAME`, `CREATED_TIME`, `CREATED_USER_ID`, `ALTERED_TIME`, `ALTERED_USER_ID`, `USER_ID`) VALUES
('consign1 belong to customer1','consign1','1',NULL,NULL,NULL,NULL,NULL,NULL,'2'),
('consign2 belong to customer1','consign2','2',NULL,NULL,NULL,NULL,NULL,NULL,'2'),
('consign3 belong to customer2','consign3','3',NULL,NULL,NULL,NULL,NULL,NULL,'3');

INSERT INTO `tbl_sys_contracts` (`CONTRACTBODY`,`PROCESS_INSTANCE_ID`,`ID`,`CODE`,`NAME`,`CREATED_TIME`,`CREATED_USER_ID`,`ALTERED_TIME`,`ALTERED_USER_ID`,`USER_ID`) VALUES
('contract1 belong to consign1','consign1','contract1',NULL,NULL,NULL,NULL,NULL,NULL,'2'),
('contract2 belong to consign2','consign2','contract2',NULL,NULL,NULL,NULL,NULL,NULL,'2'),
('contract3 belong to consign3','consign3','contract3',NULL,NULL,NULL,NULL,NULL,NULL,'3');

INSERT INTO `tbl_sys_testreports`(`ID`,`CODE`,`NAME`,`CREATED_TIME`,`CREATED_USER_ID`,`ALTERED_TIME`,`ALTERED_USER_ID`,`REPORT`) VALUES
('tr1',NULL,NULL,NULL,NULL,NULL,NULL,'testReport1'),
('tr2',NULL,NULL,NULL,NULL,NULL,NULL,'testReport2'),
('tr3',NULL,NULL,NULL,NULL,NULL,NULL,'testReport3');


INSERT INTO `tbl_sys_projects` (`ID`,`CODE`,`NAME`,`CREATED_TIME`,`CREATED_USER_ID`,`ALTERED_TIME`,`ALTERED_USER_ID`,`USER_ID`,`CONSIGN_ID`,`CONTRACT_ID`,`TESTREPORT_ID`) VALUES
('p1',NULL,'project1',NULL,NULL,NULL,NULL,'2','1','contract1','tr1'),
('p2',NULL,'project2',NULL,NULL,NULL,NULL,'2','2','contract2','tr2'),
('p3',NULL,'project3',NULL,NULL,NULL,NULL,'3','3','contract3','tr3');

