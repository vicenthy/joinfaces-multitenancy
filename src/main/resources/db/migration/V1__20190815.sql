    create table if not exists cliente (
       id bigint not null auto_increment,
        nascimento date,
        nome varchar(255),
        primary key (id)
    ) engine=InnoDB;
