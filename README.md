DISTRIBUTED BANKING SYSTEM EMULATOR
===================================
This project depicts a fault tolerant banking system. It is composed of three main entities: an ATM, a Consortium and a Bank.  
The execution flows starting from the ATM, which creates a request message to be forwarded to the Bank through the Consortium.  


FEATURES
--------
* UDP/IP as communication protocol
* Emulated outbound/inbound link failure
* Multithreaded
* Execution failure tolerant


INSTALLATION
------------
__REQUIREMENTS__
* mysql 5+
* maven 3+
* java 1.5+
* git

__QUICK GUIDE__
* Choose your desired folder location and clone the repo issuing  
`git clone https://github.com/marcos-sb/distributed-banking-system.git`  
The folder `distributed-banking-system` should have been created  

* Change directory to the one just created and maven install:  
`cd distributed-banking-system && mvn install`  

* mysql user `alba` has to be created:  
`mysql -uroot -p`  
`mysqlprompt$> create user 'alba'@'localhost' identified by 'alba'`  
`mysqlprompt$> grant all on *.* to alba@localhost identified by 'alba'`  

* mysql DBs, tables and tuples creation is scripted:  
`mysql -ualba -palba < bank/src/main/resources/bank.sql`  
`mysql -ualba -palba < consortium/src/main/resources/consortium.sql`


CONFIGURATION
-------------
A parameters file could be found at `{cashmachine, bank, consortium}/src/main/resources`. It configures networking and database connections. Note that if any value where to be changed a new `mvn install` should be issued.


RUN
---
It is advised to run each agent in a different terminal so following the message flow would be easier.  
To start execution, type (one line per terminal):  
`java -jar cashmachine/target/cashmachine-0.0.1.jar`  
`java -jar consortium/target/consortium-0.0.1.jar`  
`java -jar bank/target/bank-0.0.1.jar`


FURTHER INFO
------------
* Full design diagrams in [magicdraw folder](https://github.com/marcos-sb/distributed-banking-system/tree/master/magicdraw).
* Check out the [Manual.pdf](https://docs.google.com/file/d/0B2lmVzXW-C5UY2EzaDRKUUh3Ujg/edit?usp=sharing), browse through the code or contact me.
