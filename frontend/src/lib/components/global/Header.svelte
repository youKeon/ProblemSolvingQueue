<header>
  <a href="/problem">
    <img
      class="logo"
      src="/images/ProblemSolvingQueue_long.png"
      alt="ProblemSolvingQueue 로고"
    />
  </a>
  <menu>
    <a href="/problem">문제 리스트</a>
    <a href="/bookmark">북마크</a>
    <a href="/setting">설정</a>
  </menu>
</header>

<style>
  header {
    top: 0;
    width: 94vw;
    height: 88px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #333; /* 어두운 배경색 */
    border-radius: 0 0 8px 8px;
    margin-bottom: 24px;
    padding: 0 16px; /* 좌우 패딩 */
  }
  .logo {
    height: 56px;
    filter: brightness(10); /* 로고를 밝게 */
  }
  menu {
    display: flex;
    gap: 16px; /* 메뉴 항목 사이의 간격 */
  }
  menu a {
    color: white; /* 텍스트 색상 흰색 */
    text-decoration: none; /* 밑줄 제거 */
    padding: 8px 16px; /* 메뉴 항목 내부 패딩 */
    border-radius: 4px; /* 메뉴 항목 모서리 둥글게 */
    transition: background-color 0.3s; /* 배경색 변경 효과 */
  }
  menu a:hover {
    background-color: #444; /* 호버 시 배경색 */
  }
</style>
