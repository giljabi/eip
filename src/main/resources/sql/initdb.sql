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

drop table userinfo;

INSERT INTO userinfo (user_id, user_nm, user_pw, use_flag, created_at, created_by, updated_at, updated_by)
VALUES('admin@admin.com', 'Admin', '$2a$10$.aIOpxx1hFeQHN.Kw9m0iOt3OaNvS7.JQNJylaRPy/J5nIgnSnCCq', '1', now(), '', now(), 'admin');

-------------------------------------------------

-- 시험문제 정답, 사용하지 않음
CREATE TABLE answer (
    id SERIAL PRIMARY KEY,
    correct INT,
    examno_id INT
);

--시험과목
CREATE TABLE subject (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);
COMMENT ON COLUMN subject.name IS '과목명';
INSERT INTO public.subject ("name") VALUES
	 ('소프트웨어설계'),
	 ('소프트웨어개발'),
	 ('데이터베이스구축'),
	 ('프로그래밍언어활용'),
	 ('정보시스템구축관리');

	
--시험년도
CREATE TABLE examyear (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);
INSERT INTO public.examyear ("name") VALUES
	 ('2020'),
	 ('2021'),
	 ('2022');
-------------------------------------------------

--시험차수(24년 1차...)
CREATE TABLE examno (
    id SERIAL PRIMARY KEY,
    examyear_id INT,
    name VARCHAR(255),
    examday VARCHAR(32),
    CONSTRAINT fk_examno FOREIGN KEY (examyear_id) REFERENCES examyear(id)
);
COMMENT ON COLUMN examno.name IS '시험차수';


-- 시험문제
CREATE TABLE question (
    id SERIAL PRIMARY KEY,
    examno_id INT,
    subject_id INT,
    --answer_id INT,
    correct int,
    name VARCHAR(255),
    imageurl  varchar(255),
    content varchar(1024),
    no INT, --index key를 만들어야 하나??? 최대 100개만 있는데....
    questionimageflag boolean default false,
    choiceimageflag boolean default false,
    CONSTRAINT fk_question_examno FOREIGN KEY (examno_id) REFERENCES examno(id),
    CONSTRAINT fk_question_subject FOREIGN KEY (subject_id) REFERENCES subject(id),
    --CONSTRAINT fk_question_answer_id FOREIGN KEY (answer_id) REFERENCES answer(id),
    CONSTRAINT uq_question_examno_no UNIQUE (examno_id, no)
);
COMMENT ON COLUMN question.name IS '시험문제';
COMMENT ON COLUMN question.subject_id IS '시험과목';
COMMENT ON COLUMN question.no IS '문제번호';
COMMENT ON COLUMN question.questionimageflag IS '문제에 이미지가 포함되어 있는가:true';
COMMENT ON COLUMN question.choiceimageflag IS '선택지에 이미지가 포함되어 있는가:true';
--alter table question add column imageurl varchar(255);
--alter table question add column content varchar(1024);
--응답건수
alter table question add column replycount int default 0;
--정답건수
alter table question add column correctcount int default 0;


--시험문제 보기
CREATE TABLE choice (
    id SERIAL PRIMARY KEY,
    question_id INT,
    --examno_id INT,
    --question_no INT,
    no int, 
    name VARCHAR(255),
    imageurl VARCHAR(255),
    CONSTRAINT fk_choice_question FOREIGN KEY (question_id) REFERENCES question(id) -- question의 id 참조
    --CONSTRAINT fk_choice_examnoid FOREIGN KEY (examno_id) REFERENCES examno(id),
    --CONSTRAINT uq_choice_question_no_examno_no UNIQUE (examno_id, question_no, no) -- examno_id와 no에 대한 UNIQUE 제약 조건 추가, 동일회차 문제번호 중복불가
);
COMMENT ON COLUMN choice.name IS '선택 답 보기';
COMMENT ON COLUMN choice.question_no IS '문제번호';
COMMENT ON COLUMN choice.no IS '보기번호';

create table results (
	id serial primary key,
	createat timestamp default now(),
	uuid char(36),
	question_id int,
	answer_no int, -- 사용자가 선택한 답
	correctflag int, -- 1 정답
	constraint fk_results_question_id foreign key (question_id) references question(id)	
);
create index idx_results_uuid on results(uuid);
alter table results add column remoteip varchar(40);

create table randomquestion(
                               id serial primary key,
                               createat timestamp default now(),
                               uuid char(36),
                               question_id int
);
create index idx_randomquestion_uuid on randomquestion(uuid)


