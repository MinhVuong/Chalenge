DROP DATABASE IF EXISTS challenge;
CREATE DATABASE challenge;
USE challenge;

create table `news`(
	`id_news` int not null auto_increment,
    `id`	int,
    `content` nvarchar(10000),
    `status` int,
    `time` nvarchar(20),
    primary key(`id_news`)
);