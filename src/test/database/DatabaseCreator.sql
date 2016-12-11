CREATE database celebsArrests;
use celebsArrests;

CREATE USER osherh IDENTIFIED BY 'mysqlpass';

grant usage on *.* to osherh@localhost identified by 'mysqlpass';
grant all privileges on celebsArrests.* to osherh@localhost;

CREATE TABLE arrests (
                NAME VARCHAR(30) NOT NULL,
				ARREST_DATE DATE NOT NULL,
				REASON VARCHAR(100) NOT NULL, 
                PRIMARY KEY (NAME,ARREST_DATE,REASON)
        );

INSERT INTO arrests values('Justin Bieber','2014-01-23','suspicion of driving under the influence and driving with an expired license');
INSERT INTO arrests values('Flavor Flav','2014-01-09','speeding while driving');
INSERT INTO arrests values('Soulja Boy','2014-01-22','possession of a loaded gun');

INSERT INTO arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving');
INSERT INTO arrests values('Suge Knight','2015-01-29',"involved in a fatal hit run");
INSERT INTO arrests values('Emile Hirsch','2015-02-12',"assault charges");

INSERT INTO arrests values('Austin Chumlee Russell','2016-03-9','sexual assault');
INSERT INTO arrests values('Track Palin','2016-01-18',"charges of fourth-degree assault and interfering with the report of a domestic violence crime");
INSERT INTO arrests values('Don McLean','2015-01-17','misdemeanor domestic violence charges');
