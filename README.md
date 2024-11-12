
# My Quiz...
* 각 자격증 시험 문제를 관리, 풀이할 수 있는 프로젝트
* http://3.37.253.88:8888/

## 1. 로그인
* 문제 등록기능으로 운영자만 사용
### url: /usr/login
```text
id: admin@admin.com
password: qweqwe123
```

## 관리자 기능
### 문제 배치등록
  * 화면 캡처...

### 데이터 파일 구조
  * 기출문제는 보통 pdf로 구할 수 있으며, pdf를 text로 변환하여 문제를 등록할 수 있도록 함
  * sample code : python/exam.py
  * pdf파일 변환 SW를 사용해도 완벽하게 되지 않으므로 아래와 같은 형식으로 완성해야 함
  * {{qif:0,cif:1}}: qif: 질문지에 이미지가 있는(1:true) 경우 question.questionimageflag에 true 저장, cif: 선택지가 이미지인 경우(1:true)를 구분해서 question.choiceimageflag에 true 저장하고, 이미지 경로를 imageurl에 함께 저장
  * 문제당 5줄, 문제 1줄, 선택 4줄로 항상 구성해야 함
  * 이미지 파일은 별도 문제수정 화면에서 개별로 저장해야 함
```text
### ExamNO:8, 과목ID:2, --뒷 부분은 설명, 차수ID: table ExamNo(8) 참고, 과목:전기기사(2) 
1. εr = 81, μr = 1 인 매질의 고유 임피던스는 약 몇 Ω 인가? (단, εr은 비유전율이고 , μr은 비투자율이다.)
	1 13.9 
	2 21.9
	3 33.9 
	4 41.9
2. 강자성체의 B-H 곡선을 자세히 관찰하면 매끈한 곡선이 아니라 자속밀도가 어느 순간 급격히 계단적으로 증가 또는 감소하는 것을 알 수 있다. 이러한 현상을 무엇이라 하는가 ?
	1 퀴리점 (Curie point)
	2 자왜현상 (Magneto-striction)
	3 바크하우젠 효과(Barkhausen effect)
	4 자기여자 효과(Magnetic after effect)
{{qif:0,cif:1}}3. 진공 중에 무한 평면도체와 d(m)만큼 떨어진 곳에 선전하밀도 λ(C/m) 의 무한 직선도체가 평행하게 놓여 있는 경우 직선 도체의 단위 길이당 받는 힘은 몇 N/m 인가?
...

100. 과전류차단기로 저압전로에 사용하는 범용의 퓨즈(용품 및 생활용품 안전관리법 」에서 규정하는 것을 제외한다)의 정격전류가 16A인 경우 용단전류는 정격전류의 몇 배인가 ? (단, 퓨즈(gG)인 경우이다.)
	1 1.25 
	2 1.5
	3 1.6 
	4 1.9
correct 4332314241
correct 1343411142
correct 3242234241
correct 1342342141
correct 2434122441
correct 3124324343
correct 2124412213
correct 2144133313
correct 4312244332
correct 4423334313
```

### 문제 수정/등록
  * 화면캡처...



## 2. 문제풀이
### url: /
```text
최초 접속시 서버에서 cookie를 만들어 접속한 사용자를 구분
```

### url: /questions/random/1/0
```text
현재 예제 데이터는 정보처리기사 문제임
1: 시험종목(1: 정보처리기사)
0: 과목(0: 모든과목, 1:소프트웨어설계, 2:소프트웨어개발, 3:데이터베이스구축, 4:프로그래밍언어활용, 5:정보시스템구축관리)
```

## 3. 풀이결과
### url: /questions/results/1
* 쿠키로 구분된 사용자의 풀이 결과

| 과목             | 도전 문항수 | 정답수 | 정답율 |
|------------------|-------------|--------|--------|
| 소프트웨어개발    | 1           | 1      | 100.0% |
| 데이터베이스구축  | 1           | 1      | 100.0% |
| 프로그래밍언어활용 | 1           | 0      | 0.0%   |
| 정보시스템구축관리 | 2           | 1      | 50.0%  |


# 개발환경
* PC or MAC
* IntelliJ IDEA

### Database
```
select version();
PostgreSQL 17.0 ....
```

### JDK
```text
1.8
```

### Spring Boot
```text
2.6.1
```

## DB Model
### ER


### Schema & Init Data
* scehma: src/main/resources/sql/schema.sql
* initdata: src/main/resources/sql/initdata.sql






