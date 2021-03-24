
CREATE TABLE "DB1"."EMPLOYEE" 
   (	"ID" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"FIRST_NAME" VARCHAR2(120 BYTE), 
	"LAST_NAME" VARCHAR2(120 BYTE), 
	"DEPT_NO" VARCHAR2(120 BYTE), 
	 CONSTRAINT "EMPLOYEE_PK" PRIMARY KEY ("ID")
)

Insert into DB1.EMPLOYEE (ID,FIRST_NAME,LAST_NAME,DEPT_NO) values ('1','Sachin','Tendulkar','4');
Insert into DB1.EMPLOYEE (ID,FIRST_NAME,LAST_NAME,DEPT_NO) values ('2','Rahul','Dravid','1');
Insert into DB1.EMPLOYEE (ID,FIRST_NAME,LAST_NAME,DEPT_NO) values ('3','Yuvraj','Singh','6');
Insert into DB1.EMPLOYEE (ID,FIRST_NAME,LAST_NAME,DEPT_NO) values ('4','MS','Dhoni','3');
Insert into DB1.EMPLOYEE (ID,FIRST_NAME,LAST_NAME,DEPT_NO) values ('5','Javagal','Srinath','2');
Insert into DB1.EMPLOYEE (ID,FIRST_NAME,LAST_NAME,DEPT_NO) values ('6','Anil','Kumble','1');
Insert into DB1.EMPLOYEE (ID,FIRST_NAME,LAST_NAME,DEPT_NO) values ('7','Ajith','Agarkar','4');
Insert into DB1.EMPLOYEE (ID,FIRST_NAME,LAST_NAME,DEPT_NO) values ('8','Souvrav','Ganguly','5');
