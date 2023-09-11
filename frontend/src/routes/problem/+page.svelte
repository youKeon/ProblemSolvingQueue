<!-- 여기는 문제 리스트 페이지입니다. -->
<script lang="ts">
  // import { getProblemsList } from "../../api/api";

  // async function fetchProblems(){
  //   const pageableData = {
  //     page: 0,
  //     size: 10,
  //     sort: ["level,"]
  //   };

  //   try{
  //     const problems = await getProblemsList(pageableData);
  //     console.log('Fetched problems: ', problems);
  //   } catch (error){
  //     console.log("문제 목록 조회 중 오류 발생:", error);
  //   }
  // }

  // fetchProblems();
</script>

<!-- 
  시멘틱 태그에 ARIA (Accessible Rich Internet Applications) 속성을 이용해 
  설명을 추가하여 접근성 향상
-->

<header>
  <img class="logo" src="/images/ProblemSolvingQueue_long.png" alt="로고">
  <menu>
    <a href="/problem">문제 리스트</a>
    <a href="/bookmark">북마크</a>
    <a href="/setting">설정</a>
  </menu>
</header>

<div class="contents">
  <section class="main-part">
    <nav aria-label="주요 메뉴 검색">
      <label for="search-box" class="visually-hidden">제목 입력</label>
      <input class="search-box" id="search-box" placeholder="&nbsp;&nbsp;제목 입력">
      <div class="select-category">
        여기에 selectbox들 배치
      </div>
    </nav>
    <main aria-label="주 콘텐츠">
      <div class="list">이것은 리스트이다</div>
    </main>
    <div class="pagination">
      여기에 페이지네이션 배치
    </div>
  </section>
  <aside aria-label="사이드 메뉴">
    여기에 사이드 항목들 배치
  </aside>
</div>

<style>
  header {
    top: 0;
    width: 100%;
    height: 88px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: white;
    border-radius: 0 0 8px 8px;
    margin-bottom: 24px;
  }
  .logo {
    height: 56px;
    margin-left: 16px;
  }
  .contents {
    display: flex;
    justify-content: space-between; /* 항목들 사이에 공간을 분배 */
    margin: 0 16px;
    width: calc(100% - 32px); /* 좌우 마진 16px을 고려하여 너비를 설정 */
    box-sizing: border-box;
  }
  .main-part {
    flex-grow: 1; /* 남은 공간을 채움 */
    margin-right: 16px; /* aside와의 간격을 유지 */
  }
  nav {
    margin-bottom: 24px;
  }
  main{
    margin-bottom: 16px;
  }
  .search-box{
    height: 64px;
    width: 100%;
    margin-bottom: 24px;
    padding: 0; /* 패딩 리셋 */
    border: none; /* 보더 리셋 */
    box-sizing: border-box; /* box-sizing 속성 설정 */
  }
  .select-category {
    height: 32px;
    width: 100%;
    margin-bottom: 24px;
    background-color: white;
  }
  .list{
    height: 512px;
    width: 100%;
    background-color: white;
  }
  /* 요소를 숨기면서 스크린리더를 통해(aria 등의 대체텍스트) 읽어올 수 있도록 한다 */
  .visually-hidden {
    position: absolute;
    width: 1px;
    height: 1px;
    margin: -1px;
    padding: 0;
    overflow: hidden;
    clip: rect(0,0,0,0);
    white-space: nowrap;
    border: 0;
  }
  .pagination {
    display: flex;
    justify-content: center;
    background-color: white;
  }
  aside {
    width: 306px; /* aside에 고정 너비 부여 */
    height: 704px;
    background-color: white;
    border: 1px solid #E5E5E5;
  }
</style>
