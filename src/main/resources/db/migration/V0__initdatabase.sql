CREATE TABLE activities
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    location_id   BIGINT NOT NULL,
    CONSTRAINT pk_activities PRIMARY KEY (id)
);

CREATE TABLE comment
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    content      VARCHAR(255) NULL,
    is_destroyed BIT(1) NOT NULL,
    post_id      BIGINT NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

CREATE TABLE emergencies
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    `description` VARCHAR(255) NULL,
    is_resolved   BIT(1) NOT NULL,
    user_id       BIGINT NOT NULL,
    CONSTRAINT pk_emergencies PRIMARY KEY (id)
);

CREATE TABLE followers
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    follower_id       BIGINT NOT NULL,
    followed_id       BIGINT NOT NULL,
    relationship_type BIGINT NULL,
    CONSTRAINT pk_followers PRIMARY KEY (id)
);

CREATE TABLE images
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    url         VARCHAR(255) NULL,
    location_id BIGINT NOT NULL,
    CONSTRAINT pk_images PRIMARY KEY (id)
);

CREATE TABLE itineraries
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    title         VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    price DOUBLE NULL,
    created_at    datetime NULL,
    updated_at    datetime NULL,
    is_destroyed  BIT(1) NULL,
    user_id       BIGINT NULL,
    CONSTRAINT pk_itineraries PRIMARY KEY (id)
);

CREATE TABLE likes
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    post_id BIGINT NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

CREATE TABLE locations
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    is_visited    BIT(1) NOT NULL,
    itinerary_id  BIGINT NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE maps
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    url  VARCHAR(255) NULL,
    CONSTRAINT pk_maps PRIMARY KEY (id)
);

CREATE TABLE notifications
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    content VARCHAR(255) NULL,
    is_read BIT(1) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    content      VARCHAR(255) NULL,
    is_destroyed BIT(1) NOT NULL,
    user_id      BIGINT NOT NULL,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    email      VARCHAR(255) NULL,
    password   VARCHAR(255) NULL,
    full_name  VARCHAR(255) NULL,
    address    VARCHAR(255) NULL,
    phone      VARCHAR(255) NULL,
    gender     SMALLINT NULL,
    dob        date NULL,
    is_active  BIT(1) NOT NULL,
    created_at date NULL,
    updated_at date NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE activities
    ADD CONSTRAINT FK_ACTIVITIES_ON_LOCATION FOREIGN KEY (location_id) REFERENCES locations (id);

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE emergencies
    ADD CONSTRAINT FK_EMERGENCIES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE followers
    ADD CONSTRAINT FK_FOLLOWERS_ON_FOLLOWED FOREIGN KEY (followed_id) REFERENCES users (id);

ALTER TABLE followers
    ADD CONSTRAINT FK_FOLLOWERS_ON_FOLLOWER FOREIGN KEY (follower_id) REFERENCES users (id);

ALTER TABLE images
    ADD CONSTRAINT FK_IMAGES_ON_LOCATION FOREIGN KEY (location_id) REFERENCES locations (id);

ALTER TABLE itineraries
    ADD CONSTRAINT FK_ITINERARIES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE locations
    ADD CONSTRAINT FK_LOCATIONS_ON_ITINERARY FOREIGN KEY (itinerary_id) REFERENCES itineraries (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);