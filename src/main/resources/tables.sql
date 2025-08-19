create table if not exists movie_booking_system_users (
user_id integer AUTO_INCREMENT,
name varchar(255),
hashed_passwd varchar(255),
display_name varchar(255),
role varchar(32),
primary key (user_id)
);

--there can be genre, etc....
create table movie_booking_system_movies (
    movie_id integer AUTO_INCREMENT,
    name varchar(255),
    length integer,
    cover_img_url varchar(255),
    director varchar(255),
    `cast` varchar(255),
    `genre` varchar(255),
    introduction text,
    primary key (movie_id)
);



alter table movie_booking_system_movies modify column `cast` varchar(255);
alter table movie_booking_system_movies modify column genre varchar(255);

create table movie_booking_system_play (
    play_id integer AUTO_INCREMENT,
    theatre_id integer,
    start_from timestamp,
    end_at timestamp,
    price decimal(18,10),
    movie_id integer,
    seats_occupied integer,
    seats_total integer,
    primary key (play_id)
);

alter table movie_booking_system_play add column theatre_id integer;
alter table movie_booking_system_play add column seats_occupied integer;
alter table movie_booking_system_play add column seats_total integer;
alter table movie_booking_system_play add column hall_no integer;
alter table movie_booking_system_play modify column price decimal(18,10);

create table movie_booking_seats (
    seat_id integer AUTO_INCREMENT,
    seat_no integer,
    play_id integer,
    primary key (seat_id)
);



create table movie_booking_order (
    order_id integer AUTO_INCREMENT,
    user_id integer,
    play_id integer,
    seat_id integer,
    pay_status varchar(32),
    create_time timestamp default current_timestamp(),
    update_time timestamp default current_timestamp(),
    primary key (order_id)
);

alter table movie_booking_order add column create_time timestamp default current_timestamp();
alter table movie_booking_order add column update_time timestamp default current_timestamp();



create table rating_movie (
    movie_id integer AUTO_INCREMENT,
    total_audit_rating bigint,
    audit_rating_count integer,
    final_audit_rating decimal(6,5),
    imdb_rating decimal(2,1),
    primary key (movie_id)
);


create table comment_user (
    comment_id integer AUTO_INCREMENT,
    user_id integer,
    movie_id integer,
    has_rating tinyint,
    rating integer,
    content text,
    primary key(comment_id)
);

create table theatre_info (
    theatre_id integer AUTO_INCREMENT,
    theatre_name varchar(64),
    theatre_address varchar(255),
    theatre_address_lat decimal(18,10),
    theatre_address_lon decimal(18,10),
    primary key(theatre_id)
);
