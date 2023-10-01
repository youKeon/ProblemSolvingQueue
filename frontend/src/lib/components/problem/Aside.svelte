<script lang="ts">
  // 필요한 데이터나 메서드를 여기에 선언합니다.
  let activeMenu: string = "home";

  function handleMenuClick(menu: string) {
    activeMenu = menu;
  }

  function handleKeyPress(event: KeyboardEvent, menu: string) {
    if (event.key === "Enter") {
      handleMenuClick(menu);
    }
  }
</script>

<aside>
  {#each ["home", "addProblem", "queue", "bookmarked", "filter", "settings", "help", "info"] as menu}
    <div
      class="menu-item {activeMenu === 'menu' ? 'active' : ''}"
      on:click={() => handleMenuClick("menu")}
      on:keypress={(event) => handleKeyPress(event, "menu")}
      role="button"
      tabindex="0"
    >
      {menu}
    </div>
  {/each}
</aside>

<style>
  aside {
    width: 86%;
    height: 94%;
    background-color: #f5f5f5;
    padding: 20px;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  }

  .menu-item {
    margin: 10px 0;
    padding: 5px;
    cursor: pointer;
  }

  .menu-item.active {
    background-color: #e0e0e0;
    font-weight: bold;
  }
</style>
