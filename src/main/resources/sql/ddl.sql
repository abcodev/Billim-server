DROP TABLE IF EXISTS `member`;

CREATE TABLE `member`
(
    `member_id`         bigint primary key auto_increment COMMENT '회원번호',
    `email`             varchar(200)                                                       NOT NULL COMMENT '회원이메일',
    `password`          varchar(200) COMMENT '비밀번호',
    `name`              varchar(100)                                                       NOT NULL COMMENT '회원이름',
    `nickname`          varchar(100)                                                       NOT NULL COMMENT '닉네임',
    `address`           varchar(100) COMMENT '회원주소',
    `grade`             varchar(10)                                                        NOT NULL COMMENT '회원등급',
    `created_at`        timestamp    default current_timestamp                             NOT NULL COMMENT '가입일자',
    `updated_at`        timestamp    default current_timestamp on update current_timestamp NOT NULL COMMENT '접속일자',
    `profile_image_url` varchar(1024)                                                      NOT NULL COMMENT '프로필 url',
    `member_type`       varchar(100) default 'GENERAL'                                     NOT NULL COMMENT '회원 타입',
    `use_yn`            varchar(1)   default 'Y' COMMENT '사용여부'
);

DROP TABLE IF EXISTS `social_member`;

CREATE TABLE social_member
(
    `social_id`             bigint auto_increment primary key COMMENT '소셜회원번호',
    `provider_name`         varchar(128)                                                    NOT NULL COMMENT '소셜사',
    `account_id`            varchar(255)                                                    NOT NULL COMMENT '소셜사 ID',
    `member_id`             bigint                                                          NOT NULL COMMENT '회원번호',
    `refresh_token`         varchar(1024)                                                   NOT NULL COMMENT '리프레시 토큰',
    `refreshTokenExpiredAt` timestamp                                                       NOT NULL COMMENT '토큰 만료일자',
    `created_at`            timestamp default current_timestamp                             NOT NULL,
    `updated_at`            timestamp default current_timestamp on update current_timestamp NOT NULL
);

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product`
(
    `product_id`   bigint primary key auto_increment COMMENT '상품번호',
    `category_id`  bigint                                                           NOT NULL COMMENT '카테고리번호',
    `member_id`    bigint                                                           NOT NULL COMMENT '판매회원번호',
    `product_name` varchar(100)                                                     NOT NULL COMMENT '상품명',
    `detail`       varchar(3000)                                                    NOT NULL COMMENT '상품설명',
    `price`        bigint                                                           NOT NULL COMMENT '대여요금',
    `trade_area`   varchar(200)                                                     NULL COMMENT '직거래지역',
    `trade_method` varchar(30)                                                      NOT NULL COMMENT '거래방법',
    `deleted_yn`   varchar(1) default 'N'                                           NOT NULL COMMENT '삭제여부',
    `created_at`   timestamp  default current_timestamp                             NOT NULL COMMENT '등록일자',
    `updated_at`   timestamp  default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `product_category`;

CREATE TABLE `product_category`
(
    `category_id`   bigint       NOT NULL COMMENT '카테고리번호',
    `category_name` varchar(100) NOT NULL COMMENT '카테고리명'
);

DROP TABLE IF EXISTS product_interest;

CREATE TABLE `product_interest`
(
    `interest_id` bigint primary key auto_increment COMMENT '관심상품번호',
    `product_id`  bigint NOT NULL COMMENT '상품번호',
    `member_id`   bigint NOT NULL COMMENT '회원번호'

);

DROP TABLE IF EXISTS `block`;

CREATE TABLE `block`
(
    `member_id`  bigint                                                          NOT NULL COMMENT '회원번호',
    `target_id`  bigint                                                          NOT NULL COMMENT '차단대상번호',
    `created_at` timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at` timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `product_order`;

CREATE TABLE `product_order`
(
    `product_order_id` bigint primary key auto_increment COMMENT '대여번호',
    `product_id`       bigint                                                          NOT NULL COMMENT '상품번호',
    `member_id`        bigint                                                          NOT NULL COMMENT '구매회원번호',
    `status`           varchar(100)                                                    NOT NULL COMMENT '대여상태', # 대여중, 예약중, 취소
    `start_at`         timestamp                                                       NOT NULL COMMENT '시작일',
    `end_at`           timestamp                                                       NOT NULL COMMENT '종료일',
    `buyer_name`       varchar(100)                                                    NULL COMMENT '구매자이름',
    `buyer_address`    varchar(200)                                                    NULL COMMENT '주소',
    `buyer_phone`      varchar(100)                                                    NULL COMMENT '연락처',
    `trade_method`     varchar(50)                                                     NULL COMMENT '거래방법',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '주문일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment`
(
    `payment_id`      bigint primary key auto_increment COMMENT '결제번호',
    `order_id`        bigint                                                          NOT NULL COMMENT '대여번호',
    `coupon_issue_id` bigint                                                          NULL COMMENT '쿠폰번호',
    `point`           bigint                                                          NULL COMMENT '적립금',
    `merchant_uid`    varchar(500)                                                    NULL COMMENT '카드결제ID',
    `imp_uid`         varchar(255)                                                    NULL COMMENT 'impUID',
    `trade_method`    varchar(50)                                                     NOT NULL COMMENT '거래방법',
    `total_amount`    bigint                                                          NOT NULL COMMENT '총금액',
    `status`          varchar(50)                                                     NOT NULL COMMENT '결제여부',
    `created_at`      timestamp default current_timestamp                             NOT NULL COMMENT '결제일자',
    `updated_at`      timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `saved_point`;

CREATE TABLE `saved_point`
(
    `point_id`         bigint primary key auto_increment COMMENT '적립금번호',
    `member_id`        bigint                                                          NOT NULL COMMENT '회원번호',
    `amount`           bigint                                                          NOT NULL COMMENT '적립금액',
    `available_amount` bigint                                                          NOT NULL COMMENT '사용가능금액',
    `expired_at`       timestamp                                                       NOT NULL COMMENT '소멸일자',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '적립일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '마지막 수정일자'
);

DROP TABLE IF EXISTS `point_history`;

CREATE TABLE `point_history`
(
    `point_history_id` bigint primary key auto_increment COMMENT '적립금 사용내역 번호',
    `payment_id`       bigint                                                          NOT NULL COMMENT '결제번호',
    `saved_point_id`   bigint                                                          NOT NULL COMMENT '적립번호',
    `amount`           bigint                                                          NOT NULL COMMENT '사용 적립액',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '사용일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '마지막 수정일자'
);

DROP TABLE IF EXISTS `coupon_issue`;

CREATE TABLE `coupon_issue`
(
    `coupon_issue_id` bigint primary key auto_increment COMMENT '쿠폰번호',
    `member_id`       bigint                                                          NOT NULL COMMENT '회원번호',
    `coupon_id`       bigint                                                          NOT NULL COMMENT '쿠폰 번호',
    `status`          varchar(10)                                                     NOT NULL COMMENT '쿠폰상태',
    `created_at`      timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at`      timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '마지막 수정일자'
);

DROP TABLE IF EXISTS `coupon`;

CREATE TABLE `coupon`
(
    `coupon_id`  bigint primary key auto_increment COMMENT '쿠폰 번호',
    `name`       varchar(200)                                                    NOT NULL COMMENT '쿠폰이름',
    `rate`       bigint                                                          NOT NULL COMMENT '차감률',
    `limit_date` bigint                                                          NOT NULL COMMENT '유효기간',
    `created_at` timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at` timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '마지막 수정일자'
);

# DROP TABLE IF EXISTS `notification`;
#
# CREATE TABLE `notification`
# (
#     `notify_id`  bigint primary key auto_increment COMMENT '알림번호',
#     `member_id`  bigint                                                          NOT NULL COMMENT '수신회원번호',
#     `type`       varchar(50)                                                     NOT NULL COMMENT '알림타입',
#     `is_read`    boolean                                                         NOT NULL COMMENT '읽음여부',
#     `is_delete`  boolean                                                         NOT NULL COMMENT '삭제여부',
#     `content`    varchar(100)                                                    NOT NULL COMMENT '알림내용',
#     `created_at` timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
#     `updated_at` timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
# );

DROP TABLE IF EXISTS `chat_room`;

CREATE TABLE `chat_room`
(
    `chat_room_id`     bigint primary key auto_increment COMMENT '채팅방번호',
    `product_id`       bigint                                                          NOT NULL COMMENT '상품번호',
    `seller_id`        bigint                                                          NOT NULL COMMENT '판매회원번호',
    `seller_joined_yn` varchar(1)                                                      NOT NULL COMMENT '판매자 참가여부',
    `buyer_id`         bigint                                                          NOT NULL COMMENT '구매회원번호',
    `buyer_joined_yn`  varchar(1)                                                      NOT NULL COMMENT '구매자 참가여부',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '업데이트일자'
);

DROP TABLE IF EXISTS `chat_message`;

CREATE TABLE `chat_message`
(
    `chat_message_id` bigint primary key auto_increment COMMENT '메세지번호',
    `chat_room_id`    bigint                                                          NOT NULL COMMENT '채팅방번호',
    `sender_id`       bigint                                                          NULL COMMENT '발신회원번호',
    `message_type`    varchar(64)                                                     NOT NULL COMMENT '메시지 종류(IMAGE,TEXT,SYSTEM)',
    `message`         varchar(2000)                                                   NOT NULL COMMENT '채팅메세지',
    `read_yn`         varchar(10)                                                     NOT NULL COMMENT '읽음여부',
    `created_at`      timestamp default current_timestamp                             NOT NULL COMMENT '생성일자',
    `updated_at`      timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '수정일자'
);

DROP TABLE IF EXISTS `review`;

CREATE TABLE `review`
(
    `review_id`        bigint primary key auto_increment COMMENT '리뷰번호',
    `product_order_id` bigint                                                          NOT NULL COMMENT '상품번호',
    `content`          varchar(1000)                                                   NOT NULL COMMENT '리뷰내용',
    `star_rating`      bigint                                                          NOT NULL COMMENT '별점',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '작성일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '업데이트일자'
);

DROP TABLE IF EXISTS `image_product`;

CREATE TABLE `image_product`
(
    `image_product_id` bigint primary key auto_increment COMMENT '이미지번호',
    `product_id`       bigint    default 0                                             NOT NULL COMMENT '상품번호',
    `url`              varchar(1024)                                                   NOT NULL COMMENT '이미지 url',
    `created_at`       timestamp default current_timestamp                             NOT NULL COMMENT '작성일자',
    `updated_at`       timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '업데이트일자'
);

DROP TABLE IF EXISTS `image_chat`;

CREATE TABLE `image_chat`
(
    `image_chat_id` bigint primary key auto_increment COMMENT '이미지번호',
    `message_id`    bigint    default 0                                             NOT NULL COMMENT '상품번호',
    `url`           varchar(1024)                                                   NOT NULL COMMENT '이미지 url',
    `created_at`    timestamp default current_timestamp                             NOT NULL COMMENT '작성일자',
    `updated_at`    timestamp default current_timestamp on update current_timestamp NOT NULL COMMENT '업데이트일자'
);

