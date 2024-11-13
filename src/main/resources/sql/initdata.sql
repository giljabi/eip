INSERT INTO userinfo (user_id,user_nm,user_pw,use_flag,created_at,created_by,updated_at,updated_by) VALUES
    ('admin@admin.com','Admin','$2a$10$.aIOpxx1hFeQHN.Kw9m0iOt3OaNvS7.JQNJylaRPy/J5nIgnSnCCq','1','2024-10-24 16:51:06.814603','','2024-10-24 16:51:06.814603','admin');

INSERT INTO qname (NAME) VALUES
                                      ('정보처리기사'),
                                      ('전기기사');

INSERT INTO examyear (NAME) VALUES
                                         ('2020'),
                                         ('2021'),
                                         ('2022'),
                                         ('2023'),
                                         ('2024');

INSERT INTO examno (NAME,EXAMDAY) VALUES
                                                           ('sinagong.co.kr','2023년 02월'),
                                                           ('comcbt.com','2020년 06월'),
                                                           ('comcbt.com','2020년 08월'),
                                                           ('comcbt.com','2020년 09월'),
                                                           ('comcbt.com','2021년 03월'),
                                                           ('comcbt.com','2021년 05월'),
                                                           ('comcbt.com','2021년 08월'),
                                                           ('comcbt.com','2022년 03월'),
                                                           ('comcbt.com','2022년 04월'),
                                                           ('sinagong.co.kr','2022년 07월'),
                                                           ('sinagong.co.kr','2023년 05월');

INSERT INTO subject (NAME,QID) VALUES
                                            ('소프트웨어설계',1),
                                            ('소프트웨어개발',1),
                                            ('데이터베이스구축',1),
                                            ('프로그래밍언어활용',1),
                                            ('정보시스템구축관리',1),
                                            ('전기자기학',2),
                                            ('전력공학',2),
                                            ('전기기기',2),
                                            ('회로이론 및 제어공학',2),
                                            ('전기설비기준 및 판단기준',2);

INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 3, '검토회의 전에 요구사항 명세서를 미리 배포하여 사전 검토한 후 짧은 검토 회의를 통해 오류를 조기에 검출하는데 목적을 두는 요구 사항 검토 방법은 ?', NULL, NULL, 1, false, false, 2, 2, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 3, '코드 설계에서 일정한 일련번호를 부여하는 방식의 코드는 ?', NULL, NULL, 2, false, false, 1, 1, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 2, '객체지향 프로그램에서 데이터를 추상화하는 단위는 ?', NULL, NULL, 3, false, false, 0, 0, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 4, '데이터 흐름도 (DFD) 의 구성요소에 포함되지 않는 것은?', NULL, NULL, 4, false, false, 1, 0, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 4, '소프트웨어 설계시 구축된 플랫폼의 성능특성 분석에 사용되는 측정 항목이 아닌 것은?', NULL, NULL, 5, false, false, 0, 0, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 1, 'UML 확장 모델에서 스테레오 타입 객체를 표현할 때 사용하는 기호로 맞는 것은?', NULL, NULL, 6, false, false, 1, 0, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 2, 'GoF(Gang of Four)의 디자인 패턴에서 행위 패턴에 속하는 것은?', NULL, NULL, 7, false, false, 3, 3, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 4, '자료 사전에서 자료의 생략을 의미하는 기호는 ?', NULL, NULL, 8, false, false, 3, 0, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 3, '트랜잭션이 올바르게 처리되고 있는지 데이터를 감시하고 제어하는 미들웨어는 ?', NULL, NULL, 9, false, false, 0, 0, true, 1);
INSERT INTO question
(EXAMNO_ID, SUBJECT_ID, CORRECT, NAME, IMAGEURL, CONTENT, NO, QUESTIONIMAGEFLAG, CHOICEIMAGEFLAG, REPLYCOUNT, CORRECTCOUNT, USEFLAG, QID)
VALUES(1, 1, 2, 'UI 설계 원칙에서 누구나 쉽게 이해하고 사용할 수 있어야 한다는 것은?', NULL, NULL, 10, false, false, 0, 0, true, 1);


INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(1, 1, '1 빌드 검증', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(1, 2, '2 동료 검토', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(1, 3, '3 워크 스루', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(1, 4, '4 개발자 검토', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(2, 1, '1 연상 코드', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(2, 2, '2 블록 코드', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(2, 3, '3 순차 코드', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(2, 4, '4 표의 숫자 코드', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(3, 1, '1 메소드', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(3, 2, '2 클래스', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(3, 3, '3 상속성', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(3, 4, '4 메시지', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(4, 1, '1 process', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(4, 2, '2 data flow', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(4, 3, '3 data store', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(4, 4, '4 data dictionary', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(5, 1, '1 응답시간 (Response Time)', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(5, 2, '2 가용성 (Availability)', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(5, 3, '3 사용률 (Utilization)', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(5, 4, '4 서버 튜닝(Server Tuning)', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(6, 1, '1 《 》', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(6, 2, '2 (( ))', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(6, 3, '3 {{ }}', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(6, 4, '4 [[ ]]', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(7, 1, '1 Builder', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(7, 2, '2 Visitor', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(7, 3, '3 Prototype', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(7, 4, '4 Bridge', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(8, 1, '1 { }', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(8, 2, '2 **', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(8, 3, '3 =', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(8, 4, '4 ( )', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(9, 1, '1 RPC', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(9, 2, '2 ORB', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(9, 3, '3 TP monitor', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(9, 4, '4 HUB', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(10, 1, '1 유효성', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(10, 2, '2 직관성', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(10, 3, '3 무결성', NULL);
INSERT INTO choice
(QUESTION_ID, NO, NAME, IMAGEURL)
VALUES(10, 4, '4 유연성', NULL);




