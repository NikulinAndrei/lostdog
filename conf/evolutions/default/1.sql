# --- !Ups

set ignorecase true;

create table LOST_REQ (
  id                        bigint not null,
  title                     varchar(100) not null,
  descr                     varchar(4000) not null,
  lost_at                   date not null,
  created_at                date not null default now(),
  constraint pk_lost_req primary key (id))
;

create sequence lost_req_seq start with 100;


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop sequence if exists lost_req_seq;

drop table if exists LOST_REQ;



