
CREATE TABLE `member` (
                          MEMBER_ID	VARCHAR(50)	PRIMARY KEY comment '회원아이디',
                          PASSWORD	VARCHAR(255)	NULL comment '패스워드',
                          MANAGER_ID VARCHAR(50)	NOT NULL comment '총판아이디',
                          ROLE_ID	VARCHAR(10)	NOT NULL comment '권한아이디',
                          USE_YN	CHAR	NOT NULL comment '사용여부',
                          MEMO	varchar(2000)	NULL comment '메모',
                          DEL_YN     char      NULL default 'N' comment  '삭제여부'
);

CREATE TABLE `slot` (
                        SLOT_NO	bigint	AUTO_INCREMENT PRIMARY KEY comment '슬롯번호',
                        PAY_NO bigint comment '결제번호',
                        MEMBER_ID	VARCHAR(50)	NOT NULL comment '회원아이디',
                        PRODUCT_NAME	VARCHAR(50)	NULL comment '상품명',
                        TYPE	varchar(50)	NULL comment '검색키워드 타입',
                        KEYWORD	varchar(50)	NULL comment '검색키워드',
                        SUB_KEYWORD	varchar(50)	NULL comment '서브키워드',
                        CATEGORY_NO	bigint	NULL comment '카테고리번호',
                        MID	bigint	NULL comment 'MID값',
                        MS	bigint	NULL comment 'MS값',
                        MEMO	varchar(2000)	NULL comment '메모'
);

CREATE TABLE `pay`
(
    PAY_NO    bigint AUTO_INCREMENT PRIMARY KEY comment '결제번호',
    MEMBER_ID VARCHAR(50) NOT NULL comment '회원',
    START_DT  date        NULL comment '시작일',
    END_DT    date        NULL comment '종료일',
    TYPE	varchar(50)	NULL comment '검색키워드 타입',
    MEMO	varchar(2000)	NULL comment '메모',
    SLOT_CNT  int         NULL comment '슬롯개수',
    DEL_YN     char      NULL default 'N' comment  '삭제여부'
);
