	
CREATE TABLE SellerContactInfo (
	contactInfoID VARCHAR(255)
,	lastName VARCHAR(255)
,	firstName VARCHAR(255)
,	email VARCHAR(255)
         PRIMARY KEY contactInfoID, 
       
); 

CREATE TABLE Tag (
	tagID INT
,	items VARCHAR(255)
,	tag VARCHAR(255)
,	refCount INT
         PRIMARY KEY tagID, 
  CONSTRAINT FK_items FOREIGN KEY items REFERENCES TOTO(items), 
     
); 

CREATE TABLE Address (
	addressID VARCHAR(255)
,	street1 VARCHAR(255)
,	street2 VARCHAR(255)
,	city VARCHAR(255)
,	state VARCHAR(255)
,	zip VARCHAR(255)
,	latitude FLOAT
,	longitude FLOAT
,	COMMA VARCHAR(255)
         PRIMARY KEY addressID, 
                 
); 

CREATE TABLE FileUploadResponse (
	itemId VARCHAR(255)
,	productId VARCHAR(255)
,	message VARCHAR(255)
,	status VARCHAR(255)
,	duration VARCHAR(255)
,	durationString VARCHAR(255)
,	startDate VARCHAR(255)
,	endDate VARCHAR(255)
,	uploadSize VARCHAR(255)
,	thumbnail VARCHAR(255)
         PRIMARY KEY itemId, 
  PRIMARY KEY productId, 
                 
); 

CREATE TABLE Category (
	categoryID VARCHAR(255)
,	name VARCHAR(255)
,	description VARCHAR(255)
,	imageURL VARCHAR(255)
         PRIMARY KEY categoryID, 
       
); 

CREATE TABLE RatingBean (
	itemId VARCHAR(255)
,	grade INT
,	cf VARCHAR(255)
         PRIMARY KEY itemId, 
    CONSTRAINT FK_cf FOREIGN KEY cf REFERENCES TOTO(cf), 
 
); 

CREATE TABLE PayPalBean (
	postData VARCHAR(255)
         CONSTRAINT FK_postData FOREIGN KEY postData REFERENCES TOTO(postData), 
 
); 

CREATE TABLE ZipLocation (
	zipCode INT
,	city VARCHAR(255)
,	state VARCHAR(255)
              
); 

CREATE TABLE Item (
	itemID VARCHAR(255)
,	productID VARCHAR(255)
,	name VARCHAR(255)
,	description VARCHAR(255)
,	imageURL VARCHAR(255)
,	imageThumbURL VARCHAR(255)
,	price VARCHAR(255)
,	address VARCHAR(255)
,	contactInfo VARCHAR(255)
,	totalScore INT
,	numberOfVotes INT
,	disabled INT
,	tags VARCHAR(255)
         PRIMARY KEY itemID, 
  PRIMARY KEY productID, 
          CONSTRAINT FK_price FOREIGN KEY price REFERENCES TOTO(price), 
  CONSTRAINT FK_address FOREIGN KEY address REFERENCES TOTO(address), 
  CONSTRAINT FK_contactInfo FOREIGN KEY contactInfo REFERENCES TOTO(contactInfo), 
        CONSTRAINT FK_tags FOREIGN KEY tags REFERENCES TOTO(tags), 
 
); 

CREATE TABLE Product (
	productID VARCHAR(255)
,	categoryID VARCHAR(255)
,	name VARCHAR(255)
,	description VARCHAR(255)
,	imageURL VARCHAR(255)
         PRIMARY KEY productID, 
  PRIMARY KEY categoryID, 
       
); 

CREATE TABLE CatalogFacade (
	emf VARCHAR(255)
,	utx VARCHAR(255)
,	bDebug VARCHAR(255)
         CONSTRAINT FK_emf FOREIGN KEY emf REFERENCES TOTO(emf), 
  CONSTRAINT FK_utx FOREIGN KEY utx REFERENCES TOTO(utx), 
  CONSTRAINT FK_bDebug FOREIGN KEY bDebug REFERENCES TOTO(bDebug), 
 
); 

