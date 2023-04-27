CREATE TABLE `customer_DB`.`customer_order`
(
    `id` INT NOT NULL AUTO_INCREMENT ,
    `product` VARCHAR(255) NOT NULL ,
    `phone` VARCHAR(255) NOT NULL ,
    `address` VARCHAR(255) NOT NULL ,
    `orderStatus` VARCHAR(255) NOT NULL ,
    `quantity` INT NOT NULL ,
    `name` VARCHAR(255) NOT NULL ,
    `email` VARCHAR(255) NOT NULL ,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB;


CREATE DATABASE finished_product_DB;
CREATE TABLE `finished_product_DB`.`product_stock` 
(
    `id` INT(11) NOT NULL AUTO_INCREMENT , 
    `product` VARCHAR(255) NOT NULL , 
    `quantity` INT(11) NOT NULL , 
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB;


CREATE DATABASE production_DB;
CREATE TABLE `production_DB`.`production_order` 
(
    `id` INT(11) NOT NULL AUTO_INCREMENT , 
    `name` VARCHAR(255) NOT NULL , 
    `quantity` INT(11) NOT NULL , 
    PRIMARY KEY (`id`)
    ) ENGINE = InnoDB;


GRANT ALL privileges on finished_product_DB.* to dev@'%' identified by 'dev';
GRANT ALL privileges on production_DB.* to dev@'%' identified by 'dev';