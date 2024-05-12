
-- 사용자 정보 테이블 생성
CREATE TABLE `User`
(
    `user_id`         INT NOT NULL AUTO_INCREMENT COMMENT '회원ID',
    `sns_id`          varchar(50)  DEFAULT NULL COMMENT 'SNS ID',
    `access_token`    varchar(500) DEFAULT NULL COMMENT '액세스 토큰',
    `name`            varchar(20)  DEFAULT NULL COMMENT '이름',
    `nickname`        varchar(20)  DEFAULT NULL COMMENT '허용	닉네임',
    `email`           varchar(255) DEFAULT NULL COMMENT '이메일',
    `phone_number`    varchar(20)  DEFAULT NULL COMMENT '전화번호',
    `profile_picture` varchar(100) DEFAULT NULL COMMENT '허용 프로필 이미지',
    `platform_type`   varchar(100) DEFAULT NULL COMMENT 'sns 타입',
    `regdate`         DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
    `exit_date`       DATETIME     DEFAULT NULL COMMENT '허용 회원탈퇴 일자(여부)',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='사용자 정보';

-- 강사/멘토 정보 테이블 생성
CREATE TABLE Instructors
(
    instructors_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '강사/멘토 ID',
    user_id        INT COMMENT '회원 ID',
    name           VARCHAR(50)   NOT NULL COMMENT '이름/사업체명',
    field          CHAR(15)      NOT NULL COMMENT '희망분야(카테고리명)',
    text           VARCHAR(1000) NOT NULL COMMENT '신청 내용',
    user_type      CHAR(1) COMMENT '권한타입(강사/멘토)',
    request_status CHAR(1)  DEFAULT 1 COMMENT '신청 상황',
    regdate        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '신청일자',
    FOREIGN KEY (user_id) REFERENCES User (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='강사/멘토';

-- 카테고리 테이블 생성
CREATE TABLE Category
(
    category_id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리ID',
    category_name CHAR(15) NOT NULL COMMENT '카테고리명'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='카테고리';

-- 강의 정보 테이블 생성
CREATE TABLE Class
(
    class_id           INT AUTO_INCREMENT PRIMARY KEY COMMENT '강의ID',
    instructors_id     INT COMMENT '강사ID',
    category_id        INT COMMENT '카테고리 ID',
    class_name         VARCHAR(100) NOT NULL COMMENT '강의명',
    description        TEXT         NOT NULL COMMENT '강의 전체 설명',
    summary            VARCHAR(150) NOT NULL COMMENT '강의 요약정보 설명',
    price              INT          NOT NULL COMMENT '강의 가격',
    video              VARCHAR(100) COMMENT '강의 영상 미리보기 url',
    total_video_length INT COMMENT '강의 총 영상 길이(재생시간)',
    regdate            DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '강의 등록일',
    edit_date          DATETIME COMMENT '수정일자',
    FOREIGN KEY (instructors_id) REFERENCES Instructors (instructors_id),
    FOREIGN KEY (category_id) REFERENCES Category (category_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='강의 정보';

-- 강의 상세정보 테이블 생성
CREATE TABLE Class_Detail
(
    class_detail_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '강의 상세정보 ID',
    class_id        INT COMMENT '강의 ID',
    title           VARCHAR(100) NOT NULL COMMENT '강의 영상 상세 제목',
    video           VARCHAR(100) NOT NULL COMMENT '강의 영상 url',
    video_length    INT          NOT NULL COMMENT '강의 영상 길이(재생시간)',
    regdate         DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '동영상 등록일',
    edit_date       DATETIME COMMENT '수정일자',
    FOREIGN KEY (class_id) REFERENCES Class (class_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='강의 상세 정보';

-- 강의 이미지 테이블 생성
CREATE TABLE Class_Image
(
    class_image_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '강의 이미지 ID',
    class_id       INT COMMENT '강의 ID',
    image          VARCHAR(100) NOT NULL COMMENT '이미지 경로',
    FOREIGN KEY (class_id) REFERENCES Class (class_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='강의 이미지';

-- 강의 자료 테이블 생성
CREATE TABLE Resource
(
    resource_id     INT AUTO_INCREMENT PRIMARY KEY COMMENT '강의 자료 ID',
    class_detail_id INT COMMENT '강의 상세정보 ID',
    resource        VARCHAR(100) NOT NULL COMMENT '자료',
    FOREIGN KEY (class_detail_id) REFERENCES Class_Detail (class_detail_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='강의 자료';

-- 수강 신청 정보 테이블 생성
CREATE TABLE Enrollment_Info
(
    enrollment_id       INT AUTO_INCREMENT PRIMARY KEY COMMENT '수강 신청 정보 ID',
    user_id             INT COMMENT '회원 ID',
    class_id            INT COMMENT '강의 ID',
    enrollment_date     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '수강 신청 일자',
    enrollment_status   CHAR(1)  DEFAULT '1' COMMENT '수강상태(수료중/수료완료/취소)',
    enrollment_fee      INT NOT NULL COMMENT '수강료',
    completion_date     DATETIME COMMENT '수료일자',
    total_progress_rate INT      DEFAULT 0 COMMENT '전체 강의 진도율',
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (class_id) REFERENCES Class (class_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='수강 신청 정보';


-- 강의 학습 데이터 테이블 생성
CREATE TABLE Learning_data
(
    learning_id       INT AUTO_INCREMENT PRIMARY KEY COMMENT '강의 학습 데이터 ID',
    user_id           INT COMMENT '회원 ID',
    class_detail_id   INT COMMENT '강의 상세정보 ID',
    video_end_time    TIME COMMENT '영상 종료 시간(=마지막 강의 시청 지점)',
    progress_rate     INT     DEFAULT 0 COMMENT '한 강의에 대한 진도율',
    completion_status CHAR(1) DEFAULT '2' COMMENT '학습 완료 여부(Y/N)',
    start_date        DATETIME COMMENT '학습 시작 일자',
    end_date          DATETIME COMMENT '학습 종료 일자',
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (class_detail_id) REFERENCES Class_Detail (class_detail_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='강의 학습 데이터';


-- 관심 강의 테이블 생성
CREATE TABLE Favorite_Class
(
    favorite_class_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '관심강의 ID',
    user_id           INT COMMENT '회원 ID',
    class_id          INT COMMENT '강의 ID',
    regdate           DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '관심강의 등록일자',
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (class_id) REFERENCES Class (class_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='관심 강의';


-- 장바구니 테이블 생성
CREATE TABLE Cart
(
    cart_id      INT AUTO_INCREMENT PRIMARY KEY COMMENT '장바구니 ID',
    user_id      INT COMMENT '회원 ID',
    class_id     INT COMMENT '강의 ID',
    regdate      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '장바구니 등록일자',
    order_status CHAR(1)  DEFAULT '2' COMMENT '주문완료여부',
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (class_id) REFERENCES Class (class_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='장바구니';


-- 강의 리뷰 테이블 생성
CREATE TABLE Class_Review
(
    class_review_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '강의 리뷰 ID',
    user_id         INT COMMENT '회원 ID',
    class_id        INT COMMENT '강의 ID',
    review_score    INT           NOT NULL COMMENT '평점',
    text            VARCHAR(1500) NOT NULL COMMENT '내용',
    regdate         DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작성 일자',
    edit_date       DATETIME COMMENT '수정일자',
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (class_id) REFERENCES Class (class_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='강의 리뷰';


-- 커뮤니티 게시글 테이블 생성
CREATE TABLE Community
(
    community_id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 ID',
    user_id        INT COMMENT '회원 ID',
    community_type CHAR(1) COMMENT '게시글 유형',
    title          VARCHAR(100)   NOT NULL COMMENT '제목',
    text           VARCHAR(10000) NOT NULL COMMENT '내용',
    favorite_count INT      DEFAULT 0 COMMENT '좋아요 수',
    comment_count  INT      DEFAULT 0 COMMENT '댓글 수',
    regdate        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작성일자',
    edit_date      DATETIME COMMENT '수정일자',
    FOREIGN KEY (user_id) REFERENCES User (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='커뮤니티 게시글';

-- 커뮤니티 댓글 테이블 생성
CREATE TABLE Comment
(
    comment_id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 ID',
    community_id INT COMMENT '게시글 ID',
    user_id      INT COMMENT '회원 ID',
    text         VARCHAR(1500) NOT NULL COMMENT '내용',
    regdate      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작성일자',
    edit_date    DATETIME COMMENT '수정일자',
    FOREIGN KEY (community_id) REFERENCES Community (community_id),
    FOREIGN KEY (user_id) REFERENCES User (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='커뮤니티 댓글';


-- 커뮤니티 좋아요 테이블 생성
CREATE TABLE Favorite_Community
(
    favorite_id      INT AUTO_INCREMENT PRIMARY KEY COMMENT '커뮤니티 좋아요 ID',
    user_id          INT COMMENT '회원 ID',
    favorite_type_id INT COMMENT '좋아요 타입 고유 ID (community_id/comment_id)',
    favorite_type    CHAR(1) COMMENT '관련 좋아요 타입 (게시글 or 댓글)',
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (favorite_type_id) REFERENCES Community (community_id) -- community_id or comment_id
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='커뮤니티 좋아요';


-- 커뮤니티 사진 테이블 생성
CREATE TABLE Community_Image
(
    community_image_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '커뮤니티 사진 ID',
    community_id       INT COMMENT '게시글 ID',
    image              VARCHAR(100) NOT NULL COMMENT '이미지',
    FOREIGN KEY (community_id) REFERENCES Community (community_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='커뮤니티 사진';


-- 주문 테이블 생성
CREATE TABLE Orders
(
    orders_id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 번호 ID',
    user_id     INT COMMENT '회원 ID',
    total_price INT NOT NULL COMMENT '총 주문 금액',
    regdate     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '주문 일자',
    FOREIGN KEY (user_id) REFERENCES User (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='주문';


-- 주문 상세 테이블 생성
CREATE TABLE Order_Detail
(
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 상세 번호 ID',
    orders_id       INT COMMENT '주문 번호 ID',
    class_id        INT COMMENT '강의 ID',
    FOREIGN KEY (orders_id) REFERENCES Orders (orders_id),
    FOREIGN KEY (class_id) REFERENCES Class (class_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='주문 상세';


-- 결제 테이블 생성
CREATE TABLE Payment
(
    payment_id     INT AUTO_INCREMENT PRIMARY KEY COMMENT '결제 ID',
    orders_id      INT COMMENT '주문 번호 ID',
    payment_type   VARCHAR(10) NOT NULL COMMENT '결제 수단',
    payment_amount INT         NOT NULL COMMENT '결제 금액',
    card           VARCHAR(10) NOT NULL COMMENT '카드 정보',
    payment_status CHAR(1)  DEFAULT '1' COMMENT '결제 상태(결제완료/결제취소)',
    regdate        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '결제 일시',
    FOREIGN KEY (orders_id) REFERENCES Orders (orders_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='결제';


-- 멘토링 정보 테이블 생성
CREATE TABLE Mentoring
(
    mentoring_id     INT AUTO_INCREMENT PRIMARY KEY COMMENT '멘토링 ID',
    category_id      INT COMMENT '카테고리 ID',
    instructors_id   INT COMMENT '멘토 ID',
    title            VARCHAR(100) NOT NULL COMMENT '멘토링 제목',
    description      TEXT         NOT NULL COMMENT '멘토링 설명',
    job              VARCHAR(30)  NOT NULL COMMENT '직무',
    career           VARCHAR(30)  NOT NULL COMMENT '경력',
    current_position VARCHAR(30)  NOT NULL COMMENT '현직',
    regdate          DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '등록일자',
    edit_date        DATETIME COMMENT '수정일자',
    FOREIGN KEY (category_id) REFERENCES Category (category_id),
    FOREIGN KEY (instructors_id) REFERENCES Instructors (instructors_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='멘토링 정보';


-- 멘토링 신청 정보 테이블 생성
CREATE TABLE Mentoring_Info
(
    mentoring_info_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '멘토링 신청 정보 ID',
    mentoring_id      INT COMMENT '멘토링 ID',
    user_id           INT COMMENT '회원 ID',
    mentoring_status  CHAR(1)  DEFAULT '1' COMMENT '멘토링 신청 상태(대기중/승인/거절)',
    regdate           DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '멘토링 신청일자',
    FOREIGN KEY (mentoring_id) REFERENCES Mentoring (mentoring_id),
    FOREIGN KEY (user_id) REFERENCES User (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='멘토링 신청 정보';


-- 멘토링 리뷰 테이블 생성
CREATE TABLE Mentoring_Review
(
    mentoring_review_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '멘토링 리뷰 ID',
    user_id             INT COMMENT '회원 ID',
    mentoring_id        INT COMMENT '멘토링 ID',
    review_score        INT           NOT NULL COMMENT '평점',
    text                VARCHAR(1500) NOT NULL COMMENT '내용',
    regdate             DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작성일자',
    edit_date           DATETIME COMMENT '수정일자',
    FOREIGN KEY (user_id) REFERENCES User (user_id),
    FOREIGN KEY (mentoring_id) REFERENCES Mentoring (mentoring_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='멘토링 리뷰';
