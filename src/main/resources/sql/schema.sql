CREATE TABLE userinfo (
                          id SERIAL PRIMARY KEY,
                          user_id varchar(255) NOT NULL,-- email
                          user_nm varchar(20) NOT NULL,
                          user_pw varchar(100) NOT NULL,
                          use_flag char(1) NOT NULL,
                          created_at timestamp NOT NULL DEFAULT now(),
                          created_by varchar(20) NULL,
                          updated_at timestamp NULL,
                          updated_by varchar(20) NULL,
                          CONSTRAINT userinfo_uk unique (user_id)
);

-- 자격증 종류
CREATE TABLE qname (
                       id serial4 PRIMARY KEY,
                       "name" varchar(128) NULL
);


-- 시행년도
CREATE TABLE examyear (
                          id serial4 PRIMARY KEY,
                          "name" varchar(255) NULL
);

-- 시험차수
CREATE TABLE examno (
                        id serial4 PRIMARY KEY,
                        examyear_id int4 NULL,
                        "name" varchar(255) NULL,
                        examday varchar(32) NULL
);
ALTER TABLE examno ADD CONSTRAINT fk_examno FOREIGN KEY (examyear_id) REFERENCES examyear(id);


--시험과목
CREATE TABLE subject (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255),
                         qid int4 NULL
);
COMMENT ON COLUMN subject.name IS '과목명';
ALTER TABLE subject ADD CONSTRAINT fk_subject_qid FOREIGN KEY (qid) REFERENCES qname(id);

-- 질문지, 자격증(qid), 시험차수(examno_id), 문제지번호(no)는 중복불가
CREATE TABLE question (
                          id serial4 PRIMARY KEY,
                          examno_id int4 NULL,
                          subject_id int4 NULL,
                          correct int4 NULL,
                          "name" varchar(255) NULL,
                          imageurl varchar(255) NULL,
                          "content" varchar(1024) NULL,
                          "no" int4 NULL,
                          questionimageflag bool DEFAULT false NULL,
                          choiceimageflag bool DEFAULT false NULL,
                          replycount int4 DEFAULT 0 NULL,
                          correctcount int4 DEFAULT 0 NULL,
                          useflag bool DEFAULT true NULL,
                          qid int4 NULL,
                          CONSTRAINT uq_question_qid_examno_no UNIQUE (qid, examno_id, no)
);
ALTER TABLE question ADD CONSTRAINT fk_question_examno FOREIGN KEY (examno_id) REFERENCES examno(id);
ALTER TABLE question ADD CONSTRAINT fk_question_qid FOREIGN KEY (qid) REFERENCES qname(id);
ALTER TABLE question ADD CONSTRAINT fk_question_subject FOREIGN KEY (subject_id) REFERENCES subject(id);

--- 문제에 연결된 선택지
CREATE TABLE choice (
                        id serial4 PRIMARY KEY,
                        question_id int4 NULL,
                        "no" int4 NULL,
                        "name" varchar(255) NULL,
                        imageurl varchar(255) NULL
);
ALTER TABLE choice ADD CONSTRAINT fk_choice_question FOREIGN KEY (question_id) REFERENCES question(id);


--사용자에게 제출된 문제 목록
CREATE TABLE randomquestion (
                                id serial4 primary key,
                                createat timestamp DEFAULT now() NULL,
                                "uuid" bpchar(36) NULL,
                                question_id int4 NULL,
                                remoteip varchar(40) NULL
);
create index idx_randomquestion_uuid on randomquestion(uuid);

-- 퀴즈결과정보, cookie(uuid정보)
CREATE TABLE results (
                         id serial4 primary key,
                         createat timestamp DEFAULT now() NULL,
                         "uuid" bpchar(36) NULL,
                         question_id int4 NULL,
                         answer_no int4 NULL,
                         correctflag int4 NULL,
                         remoteip varchar(40) NULL,
                         qid int4 NULL
);
CREATE INDEX idx_results_uuid ON results USING btree (uuid);
ALTER TABLE results ADD CONSTRAINT fk_results_question_id FOREIGN KEY (question_id) REFERENCES question(id);

-- gpt사용 결과, 날짜별 토큰 사용량만 계산, 토큰 사용량은 1일 10000개 초과시 중단
CREATE TABLE tokenusage (
                            id SERIAL PRIMARY KEY,
                            date VARCHAR(10) NOT NULL UNIQUE,  -- yyyy-MM-dd 형식
                            prompt_tokens INTEGER NOT NULL DEFAULT 0,
                            completion_tokens INTEGER NOT NULL DEFAULT 0,
                            total_tokens INTEGER NOT NULL DEFAULT 0
);
alter table tokenusage add column reqcnt int default 0; --요청건수
