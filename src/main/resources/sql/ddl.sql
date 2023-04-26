DROP TABLE IF EXISTS `member`;

CREATE TABLE `member`
(
    `member_id`         int primary key auto_increment COMMENT '회원번호',
    `user_id`           varchar(100)                                                    NOT NULL COMMENT '회원ID',
    `password`          varchar(200)                                                    NOT NULL COMMENT '비밀번호',
    `name`              varchar(100)                                                    NOT NULL COMMENT '회원이름',
    `nickname`          varchar(100)                                                    NOT NULL COMMENT '닉네임',
    `address`           varchar(100)                                                    NOT NULL COMMENT '회원주소',
    `email`             varchar(200)                                                    NOT NULL COMMENT '회원이메일',
    `grade`             varchar(10)                                                     NOT NULL COMMENT '회원등급',
    `created_at`        timestamp default current_timestamp                             NOT NULL COMMENT '가입일자',
    `updated_at`        timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '접속일자',
    `profile_image_url` varchar(1024)                                                   NOT NULL COMMENT '프로필 url'
);

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product`
(
    `product_id`   int primary key auto_increment COMMENT '상품번호',
    `category_id`  int                                                             NOT NULL COMMENT '카테고리번호',
    `member_id`    int                                                             NOT NULL COMMENT '판매회원번호',
    `product_name` varchar(100)                                                    NOT NULL COMMENT '상품명',
    `detail`       varchar(3000)                                                   NOT NULL COMMENT '상품설명',
    `price`        int                                                             NOT NULL COMMENT '대여요금',
    `created_at`   timestamp default current_timestamp                             NOT NULL COMMENT '등록일자',
    `updated_at`   timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자',
    `trade_method` varchar(30)                                                     NOT NULL COMMENT '거래방법'
);

DROP TABLE IF EXISTS `product_category`;

CREATE TABLE `product_category`
(
    `category_id`   int          NOT NULL COMMENT '카테고리번호',
    `category_name` varchar(100) NOT NULL COMMENT '카테고리명'
);

DROP TABLE IF EXISTS product_interest;

CREATE TABLE `product_interest`
(
    `interest_id` int primary key auto_increment COMMENT '관심상품번호',
    `product_id`  int NOT NULL COMMENT '상품번호',
    `member_id`   int NOT NULL COMMENT '회원번호'

);

DROP TABLE IF EXISTS `block`;

CREATE TABLE `block`
(
    `member_id`  int                                                             NOT NULL COMMENT '회원번호',
    `target_id`  int                                                             NOT NULL COMMENT '차단대상번호',
    `created_at` timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at` timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `product_order`;

CREATE TABLE `product_order`
(
    `product_order_id`   int primary key auto_increment COMMENT '대여번호',
    `product_id` int                                                             NOT NULL COMMENT '상품번호',
    `member_id`  int                                                             NOT NULL COMMENT '구매회원번호',
    `status`     varchar(10)                                                     NOT NULL COMMENT '대여상태', # 대여중, 예약중, 취소
                     `start_at`   timestamp                                                       NOT NULL COMMENT '시작일',
    `end_at`     timestamp                                                       NOT NULL COMMENT '종료일',
    'buyer_name' varchar(100) NULL COMMENT '구매자이름',
    `buyer_address`    varchar(200)                                                    NULL COMMENT '주소',
    `buyer_phone`      varchar(100)                                                    NULL COMMENT '연락처',
    'trade_method' varchar(50) NULL COMMENT '거래방법',
    `created_at` timestamp default current_timestamp                             NOT NULL COMMENT '주문일자',
    `updated_at` timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment`
(
    `payment_id`      int primary key auto_increment COMMENT '결제번호',
    `order_id`        int                                                             NOT NULL COMMENT '대여번호',
    `coupon_issue_id` int                                                             NULL COMMENT '쿠폰번호',
    `point`           int                                                             NULL COMMENT '적립금',
    `merchant_uid`    varchar(500)                                                    NULL COMMENT '카드결제ID',
    `total_amount`    int                                                             NOT NULL COMMENT '총금액',
    `status`          varchar(50)                                                         NOT NULL COMMENT '결제여부',
    `trade_method`    varchar(50)                                                     NOT NULL COMMENT '거래 방법',
    'imp_uid' varchar(255) NOT NULL COMMENT 'impUID',
    `created_at`      timestamp default current_timestamp                             NOT NULL COMMENT '결제일자',
    `updated_at`      timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `saved_point`;

CREATE TABLE `saved_point`
(
    `point_id`         int primary key auto_increment COMMENT '적립금번호',
    `member_id`        int                                                             NOT NULL COMMENT '회원번호',
    `amount`           int                                                             NOT NULL COMMENT '적립금액',
    `available_amount` int                                                             NOT NULL COMMENT '사용가능금액',
    `expired_at`       timestamp                                                       NOT NULL COMMENT '소멸일자',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '적립일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '마지막 수정일자'
);

DROP TABLE IF EXISTS `point_history`;

CREATE TABLE `point_history`
(
    `point_history_id` int primary key auto_increment COMMENT '적립금 사용내역 번호',
    `payment_id`       int                                                             NOT NULL COMMENT '결제번호',
    `saved_point_id`   int                                                             NOT NULL COMMENT '적립번호',
    `amount`           int                                                             NOT NULL COMMENT '사용 적립액',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '사용일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '마지막 수정일자'
);

DROP TABLE IF EXISTS `coupon_issue`;

CREATE TABLE `coupon_issue`
(
    `coupon_issue_id` int primary key auto_increment COMMENT '쿠폰번호',
    `member_id`       int                                                             NOT NULL COMMENT '회원번호',
    `coupon_id`       int                                                             NOT NULL COMMENT '쿠폰 번호',
    `status`          varchar(10)                                                     NOT NULL COMMENT '쿠폰상태',
    `created_at`      timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at`      timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '마지막 수정일자'
);

DROP TABLE IF EXISTS `coupon`;

CREATE TABLE `coupon`
(
    `coupon_id`  int primary key auto_increment COMMENT '쿠폰 번호',
    `name`       varchar(200)                                                    NOT NULL COMMENT '쿠폰이름',
    `rate`       int                                                             NOT NULL COMMENT '차감률',
    `limit_date` int                                                             NOT NULL COMMENT '유효기간',
    `created_at` timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at` timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '마지막 수정일자'
);

DROP TABLE IF EXISTS `chat_room`;

CREATE TABLE `chat_room`
(
    `chat_room_id` int primary key auto_increment COMMENT '채팅방번호',
    `product_id`   int                                                             NOT NULL COMMENT '상품번호',
    `member_id`    int                                                             NOT NULL COMMENT '구매회원번호',
    `created_at`   timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at`   timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '업데이트일자'
);

DROP TABLE IF EXISTS `notification`;

CREATE TABLE `notification`
(
    `notify_id`  int primary key auto_increment COMMENT '알림번호',
    `member_id`  int                                                             NOT NULL COMMENT '수신회원번호',
    `type`       varchar(50)                                                     NOT NULL COMMENT '알림타입',
    `is_read`    boolean                                                         NOT NULL COMMENT '읽음여부',
    `is_delete`  boolean                                                         NOT NULL COMMENT '삭제여부',
    `content`    varchar(100)                                                    NOT NULL COMMENT '알림내용',
    `created_at` timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at` timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `chat_message`;

CREATE TABLE `chat_message`
(
    `message_id`   int primary key auto_increment COMMENT '메세지번호',
    `chat_room_id` int                                                             NOT NULL COMMENT '채팅방번호',
    `member_id`    int                                                             NOT NULL COMMENT '발신회원번호',
    `message`      varchar(2000)                                                   NOT NULL COMMENT '채팅메세지',
    `is_read`      boolean                                                         NOT NULL COMMENT '읽음여부',
    `created_at`   timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at`   timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `review`;

CREATE TABLE `review`
(
    `review_id`   int primary key auto_increment COMMENT '리뷰번호',
    `product_id`  int                                                             NOT NULL COMMENT '상품번호',
    `member_id`   int                                                             NOT NULL COMMENT '구매회원번호',
    `content`     varchar(1000)                                                   NOT NULL COMMENT '리뷰내용',
    `star_rating` int                                                             NOT NULL COMMENT '별점',
    `created_at`  timestamp default current_timestamp                             NOT NULL COMMENT '작성일자',
    `updated_at`  timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '업데이트일자',
    `status`      varchar(10)                                                     NOT NULL COMMENT '삭제여부'
);

DROP TABLE IF EXISTS `image_product`;

CREATE TABLE `image_product`
(
    `image_product_id` int primary key auto_increment COMMENT '이미지번호',
    `product_id`       int       default 0                                             NOT NULL COMMENT '상품번호',
    `url`              varchar(1024)                                                   NOT NULL COMMENT '이미지 url',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '작성일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '업데이트일자'
);

DROP TABLE IF EXISTS `image_chat`;

CREATE TABLE `image_chat`
(
    `image_chat_id` int primary key auto_increment COMMENT '이미지번호',
    `message_id`    int                                                             NOT NULL COMMENT '메세지번호',
    `url`           varchar(1024)                                                   NOT NULL COMMENT '이미지 url',
    `created_at`    timestamp default current_timestamp                             NOT NULL COMMENT '작성일자',
    `updated_at`    timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '업데이트일자'
);