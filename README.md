
# My Quiz...
* 각 자격증 시험 문제를 관리, 풀이할 수 있는 웹 서비스입니다.

## 1. 로그인
### url: /usr/login
```text
id: admin@admin.com
password: qweqwe123
```
- 
### 관리자 기능
    - 로그인
    - 문제 배치등록
    - 문제 수정/등록

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






