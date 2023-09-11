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

<header>
  <img class="logo" src="/images/ProblemSolvingQueue_long.png" alt="로고">
</header>

<div class="contents">
  <div class="main-part">
    <nav>
      <input class="search-box" id="search-box" placeholder="제목 입력">
      <div class="select-category">
        여기에 selectbox들 배치
      </div>
    </nav>
    <main>
      <div class="list">이것은 리스트이다</div>
    </main>
  </div>
  <aside>
    여기에 사이드 항목들 배치
  </aside>
</div>

<style>
  header{
    top: 0;
    width: 100%;
    height: 88px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: white;
    border-radius: 8;
    margin-bottom: 24px;
  }
  nav{
    margin-bottom: 24px;
  }
  header .logo{
    height: 56px;
  }

  .contents{
    display: flex;
    justify-content: space-between;
    margin : 0 16px;
  }
</style>
