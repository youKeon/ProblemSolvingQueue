<script lang="ts">
  export let currentPage = 1;
  export let totalPage = 5;

  function handleClick(page:number){
    if(page >= 1 && page <= totalPage){
      currentPage = page;
      // 여기서 현재 페이지에 대한 데이터 불러오기 함수
      // fetchProblems(currentPage);
    }
  }
</script>

<div class="pagination">
  <button class="pagination-button" disabled={currentPage === 1} on:click={() => handleClick(currentPage - 1)}>이전</button>
  {#each Array(totalPage) as _, i}
    <button class="pagination-button" class:active={currentPage === i + 1} on:click={() => handleClick(i + 1)}>{i + 1}</button>
  {/each}
  <button class="pagination-button" disabled={currentPage === totalPage} on:click={() => handleClick(currentPage + 1)}>다음</button>
</div>

<style>
  .pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 16px;
  }
  
  .pagination-button {
    margin: 0 4px;
    padding: 8px 16px;
    border: 1px solid #ccc;
    background-color: #fff;
    cursor: pointer;
    transition: background-color 0.3s, border-color 0.3s; /* 트랜지션 효과 추가 */
  }
  
  .pagination-button:hover {
    background-color: #f0f0f0;
    border-color: #aaa; /* 호버 시 테두리 색 변경 */
  }

  .pagination-button:disabled {
    color: #ccc;
    cursor: not-allowed;
  }

  .pagination-button:disabled:hover {
    background-color: #fff; /* 비활성화 상태에서는 배경색 변경 안함 */
    border-color: #ccc; /* 비활성화 상태에서는 테두리 색 변경 안함 */
  }

  .pagination-button.active {
    background-color: #e0e0e0;
  }
</style>