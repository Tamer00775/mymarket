alter table orders add column transaction_id int references user_transaction_history(id);
alter table user_transaction_history add column card_num varchar;