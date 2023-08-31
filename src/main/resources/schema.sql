create table customer_review(
   id int not null AUTO_INCREMENT,
   review  varchar(5000),
   author varchar(60),
   review_source varchar(20),
   rating int,
   title varchar(100),
   product_name varchar(50),
   reviewed_date TIMESTAMP,
   PRIMARY KEY ( id )
);