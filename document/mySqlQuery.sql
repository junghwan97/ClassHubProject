CREATE TABLE `User` (
  `user_id` INT NOT NULL AUTO_INCREMENT COMMENT '회원ID',
  `sns_id` varchar(50) DEFAULT NULL COMMENT 'SNS ID',
  `access_token` varchar(500) DEFAULT NULL COMMENT '액세스 토큰',
  `name` varchar(20) DEFAULT NULL COMMENT '이름',
  `nickname` varchar(20) DEFAULT NULL COMMENT '허용	닉네임',
  `email` varchar(255) DEFAULT NULL COMMENT '이메일',
  `phone_number` varchar(20) DEFAULT NULL COMMENT '전화번호',
  `profile_picture` varchar(100) DEFAULT NULL COMMENT '허용 프로필 이미지',
  `platform_type` varchar(100) DEFAULT NULL COMMENT 'sns 타입',
  `regdate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
  `exit_date` DATETIME DEFAULT NULL COMMENT '허용 회원탈퇴 일자(여부)',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='사용자 정보';

