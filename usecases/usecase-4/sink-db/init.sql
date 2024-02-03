create table if not exists clients (
    id SERIAL PRIMARY KEY,
    client_id varchar(150) not null,
    name varchar(150) not null,
    age varchar(150) not null
);


create table if not exists products (
    id SERIAL PRIMARY KEY,
    product_id varchar(150) not null,
    title varchar(150) not null,
    amount varchar(150) not null,
    price varchar(150) not null,
    customer_id varchar(150) not null
);

