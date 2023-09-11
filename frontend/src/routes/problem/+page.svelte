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
</header>

<div class="contents">
  <section class="main-part">
    <nav aria-label="주요 메뉴">
      <label for="search-box" class="visually-hidden">제목 입력</label>
      <input class="search-box" id="search-box" placeholder="제목 입력">
      <div class="select-category">
        여기에 selectbox들 배치
      </div>
    </nav>
    <main aria-label="주 콘텐츠">
      <div class="list">이것은 리스트이다</div>
    </main>
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
    border-radius: 8px;
    margin-bottom: 24px;
  }
  .logo {
    height: 56px;
  }
  nav {
    margin-bottom: 24px;
  }
  .contents {
    display: flex;
    justify-content: space-between;
    margin: 0 16px;
  }
  .search-box {
    margin-bottom: 24px;
  }
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
</style>
