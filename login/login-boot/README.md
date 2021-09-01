https://www.javainuse.com/spring/boot_form_security

## howto

    
    docker exec -it mysql bash
    mysql -u root -p

    create database mydb;
    create user 'curt'@'%' IDENTIFIED WITH mysql_native_password BY 'curt';
    grant all privileges on mydb.* to 'curt'@'%';

