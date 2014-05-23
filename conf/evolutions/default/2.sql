# --- !Ups

set ignorecase true;

create table LOST_REQ_IMG (
  id                        bigint not null,
  req_id                    bigint not null,
  location                  varchar(100) not null,
  created_at                date not null default now(),
  constraint pk_lost_req_img primary key (id),
  constraint fk1_lost_req_img foreign key(req_id) references lost_req(id)
  )
;

create sequence lost_req_img_seq start with 100;


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop sequence if exists lost_req_img_seq;

drop table if exists lost_req_img;



