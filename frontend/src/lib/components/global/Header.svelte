<script lang="ts">
  export let title: string;
</script>

<header>
  <a href="/problem">
    <img class="logo" src="/images/ProblemSolvingQueue_long.png" alt="로고" />
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
  menu{
    margin-right: 16px;
  }
</style>