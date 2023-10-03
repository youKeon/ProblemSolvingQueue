<script lang="ts">
  // 필요한 데이터나 메서드를 여기에 선언합니다.
  let activeMenu: string = "home";

  const menus = [
    { id: "home", label: "홈 (Home)" },
    { id: "addProblem", label: "문제 추가 (Add Problem)" },
    { id: "queue", label: "Queue" },
    { id: "bookmarked", label: "북마크 문제들 (Bookmarked Problems)" },
    { id: "filter", label: "문제 유형별 조회 (Filter by Problem Type)" },
    { id: "settings", label: "설정 (Settings)" },
    { id: "help", label: "도움말 & FAQ (Help & FAQ)" },
    { id: "info", label: "프로젝트 정보 (Project Info)" },
  ];

  function handleMenuClick(menuId: string) {
    activeMenu = menuId;
  }

  function handleKeyPress(event: KeyboardEvent, menuId: string) {
    if (event.key === "Enter") {
      handleMenuClick(menuId);
    }
  }
</script>

<aside>
  {#each menus as menu}
    <div
      class="menu-item {activeMenu === menu.id ? 'active' : ''}"
      on:click={() => handleMenuClick(menu.id)}
      on:keypress={(event) => handleKeyPress(event, menu.id)}
      role="button"
      tabindex="0"
    >
      {menu.label}
    </div>
  {/each}
</aside>

<style>
  aside {
    width: 86%;
    height: 94%;
    background-color: #333; /* 어두운 회색 배경 */
    padding: 20px;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
    color: #fff; /* 텍스트 색상 흰색 */
  }

  .menu-item {
    margin: 10px 0;
    padding: 8px 10px; /* 패딩 증가 */
    cursor: pointer;
    font-size: 1.1em; /* 폰트 사이즈 조정 */
    transition: background-color 0.3s; /* 부드러운 전환 효과 */
  }

  .menu-item:hover {
    background-color: #555; /* 호버 시 색상 변경 */
  }

  .menu-item.active {
    background-color: #e0e0e0;
    color: #333; /* 선택된 항목의 텍스트 색상 변경 */
    font-weight: bold;
    border-left: 4px solid #ffd700; /* 선택된 항목에 좌측 테두리 추가 */
  }
</style>
