<!-- 여기는 문제 리스트 페이지입니다. -->
<script lang="ts">
  import SelectBox from "$lib/components/elements/SelectBox.svelte";
  import ProblemTable from "$lib/components/problem/ProblemTable.svelte";
  import Pagination from "$lib/components/elements/Pagination.svelte";
  import { problems } from "$lib/data/mock/problemsData";
  import Aside from "$lib/components/problem/Aside.svelte";

  const bookmarkOptions: { value: string; text: string }[] = [
    { value: "all", text: "전체" },
    { value: "bookmark", text: "북마크" },
    { value: "not-bookmark", text: "북마크 아님" },
  ];

  const statusOptions: { value: string; text: string }[] = [
    { value: "all", text: "전체" },
    { value: "solved", text: "해결" },
    { value: "unsolved", text: "미해결" },
  ];

  const levelOptions: { value: string; text: string }[] = [
    { value: "all", text: "전체" },
    { value: "easy", text: "쉬움" },
    { value: "medium", text: "중간" },
    { value: "hard", text: "어려움" },
  ];
</script>

<!-- 
  시멘틱 태그에 ARIA (Accessible Rich Internet Applications) 속성을 이용해 
  설명을 추가하여 접근성 향상
-->

<div class="contents">
  <section class="main-part">
    <nav aria-label="주요 메뉴 검색">
      <label for="search-box" class="visually-hidden">제목 입력</label>
      <input class="search-box" id="search-box" placeholder="제목 입력" />
      <div class="select-category">
        <div class="select-box">filter</div>
        <div class="select-box">
          <SelectBox id="bookmark" label="북마크" options={bookmarkOptions} />
        </div>
        <div class="select-box">
          <SelectBox id="status" label="상태" options={statusOptions} />
        </div>
        <div class="select-box">
          <SelectBox id="level" label="난이도" options={levelOptions} />
        </div>
      </div>
    </nav>
    <main aria-label="문제 리스트 모아보기">
      <ProblemTable {problems} />
    </main>
    <!-- 페이지네이션 위치 -->
    <div class="pagination-aria">
      <Pagination currentPage={1} totalPage={5} />
    </div>
  </section>
  <aside aria-label="사이드 메뉴">
    <Aside />
  </aside>
</div>

<style>
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
  main {
    height: 512px;
  }
  aside {
    display: flex;
    justify-content: center;
  }
  .search-box {
    height: 64px;
    width: 100%;
    margin-bottom: 24px;
    font-size: x-large;
    padding: 0; /* 패딩 리셋 */
    padding-left: 16px;
    border: none; /* 보더 리셋 */
    box-sizing: border-box; /* box-sizing 속성 설정 */
  }
  .select-category {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    height: 32px;
    width: 100%;
    margin-bottom: 24px;
  }
  .select-box {
    margin-right: 16px;
  }
  /* 요소를 숨기면서 스크린리더를 통해(aria 등의 대체텍스트) 읽어올 수 있도록 한다 */
  .visually-hidden {
    position: absolute;
    width: 1px;
    height: 1px;
    margin: -1px;
    padding: 0;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border: 0;
  }
  .pagination-aria {
    display: flex;
    justify-content: center;
    margin-top: -16px;
  }
  aside {
    width: 306px; /* aside에 고정 너비 부여 */
    height: 704px;
    background-color: white;
    border: 1px solid #e5e5e5;
  }
</style>
