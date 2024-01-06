# 💡 소개

- 코딩 테스트 문제 관리 서비스
- 문제를 저장하고 알고리즘 유형, 문제 난이도, 풀이 여부 등으로 문제를 검색하는 서비스
- 서비스 종료

# ⭐️ 핵심 기능

- 문제 검색 기능
    - 알고리즘 유형, 문제 난이도, 풀이 여부 등으로 원하는 문제를 검색
- 문제 추천 기능
    - 매일 아침 11시에 두 개의 문제를 Slack으로 발송
    - `추천 기준`
        - 사용자가 저장한 문제들의 평균 난이도가 가장 높은 알고리즘 유형 조회
        - 해당 알고리즘 유형의 평균 난이도를 기준으로 +/- 1 값을 가지는 두 개의 문제를 발송
        - 만약, 문제 난이도가 동일하다면 풀이 수가 적은 값이 우선순위를 가짐
        - `예시`
            - 사용자 A는 DFS 문제의 평균 난이도가 4로 해당 유형을 가장 어려워한다.
            - 사용자 A가 저장한 DFS 문제들 중 난이도가 3인 문제와 5인 문제를 Slack으로 발송한다.

# 🏗️ 시스템 아키텍처
![Frame 1](https://github.com/youKeon/ProblemSolvingQueue/assets/96862049/3e05dfe1-dd85-4aea-9362-bfec882fa47f)

# 🛠️ 기술 스택
### 백엔드
![Frame 3](https://github.com/youKeon/ProblemSolvingQueue/assets/96862049/f5115739-9145-4fcc-8ea2-9517d543447c)

### 인프라
![Frame 4](https://github.com/youKeon/ProblemSolvingQueue/assets/96862049/69586833-ddeb-4dca-a2b2-d57e2bc99ea0)


# 🔍 ERD
<img width="655" alt="2" src="https://github.com/youKeon/ProblemSolvingQueue/assets/96862049/87d2d143-e896-48dd-9f4f-f3cc59ad008a">



# 💻 TEAM

- 백엔드 1명
- 프론트엔드 1명
