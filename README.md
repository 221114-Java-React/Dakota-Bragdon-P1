# Dakota-Bragdon-P1

## Ticketer Description
Tickter is a ticketing reimbursement system which allows employees to submit tickets that managers can approve or deny and logs while also saving all tickets (and users) to a database.

Ticketer is a RESTful (REpresentational State Transfer) Application Program Interface with multiple URI and HTTP verbs, making it a Level 2 on the Richardson API Maturity Model. The program is RESTful since the client-server relationship is managed through HTTP requests, there is stateless communication (meaning no client information is stored on the server), data is cacheable, and there is uniform interface (JSON API). Authentication is handled on the client side with the usage of tokens and any critical data, such as passwords, are encrypted.

There are three different roles that can be assigned to users, with each having their own corresponding utilities.

#### Admins 
Admins control access to the system. All new users that register to the system must first be granted access by an admin. Access can also be revoked by an admin as well. Users can also have their passwords 'reset' by an admin (the admin will basically set a new password for the user).

#### Managers 
Managers can view all reimbursement tickets, their details, and can resolve tickets by setting their status to either APPROVE or DENIED. Tickets are ordered based on status, type, and the amount. Once a pending ticket has been resolved, no further alterations can be made to that ticket. Managers can also look at all the tickets they have resolved and look at all the tickets for a single user.

#### Employees 
Employees can submit new tickets for a manager to approve. These tickets must be of type LODGING, TRAVEL, FOOD, OTHER and will automatically be assigned a status of PENDING. Employees can view their tickets and make changes prior to the tickets being resolved, or finalized by a manager.

### System Use Diagram
![UserStory](https://user-images.githubusercontent.com/118224708/205525706-6a2def21-e813-407f-b070-f20ccd30b6e5.jpg)

### Database Design Specifications
The Ticketer database satisfies the Third Normaization Form (3NF) requirements. It does so, because the data contained within has unique identifiers and is atomic, columns only depend on primary keys, and there are no partial dependencies.

### Relational Data Model
![Relational Data Model](https://user-images.githubusercontent.com/118224708/205525753-df456cb7-8042-44e0-bd3c-8604efa847a4.jpg)

#### Tech Stack
#### Application Tier
* Java 8 - Primary Programming Language
* Apache Maven  - Dependency Manager
* JDBC - Database Connectivity API 
* JSON Web Tokens - Payload Encryption
* JUnit - Unit Testing Framework
* Mockito - Mocking Framework

#### Persistence Tier
* PostGreSQL (running on Docker)

### System Flow
![FlowChart](https://user-images.githubusercontent.com/118224708/205535837-3c61b330-4361-45c6-b986-0b0793384e0c.jpg)
