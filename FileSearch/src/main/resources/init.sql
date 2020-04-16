drop table if exists file_meta;

create table if not exists file_meta(
    name varchar(50) not null,          /*文件名称*/
    path varchar(1000) not null,        /*文件路径*/
    is_directory boolean not null,      /*是否是文件夹*/
    size bigint not null,               /*文件大小*/
    last_modified timestamp not null,   /*文件最后一次修改时间*/
    pinyin varchar(50),                 /*文件名拼音*/
    pinyin_first varchar(50)            /*文件名拼音首字母*/
);