<script lang="ts">
  export let currentPage = 1;
  export let totalPage = 5;

  function handleClick(page: number) {
    if (page >= 1 && page <= totalPage) {
      currentPage = page;
      // 여기서 현재 페이지에 대한 데이터 불러오기 함수
      // fetchProblems(currentPage);
    }
  }
</script>

<div class="pagination">
  <button
    class="pagination-button"
    disabled={currentPage === 1}
    on:click={() => handleClick(currentPage - 1)}>이전</button
  >
  {#each Array(totalPage) as _, i}
    <button
      class="pagination-button"
      class:active={currentPage === i + 1}
      on:click={() => handleClick(i + 1)}>{i + 1}</button
    >
  {/each}
  <button
    class="pagination-button"
    disabled={currentPage === totalPage}
    on:click={() => handleClick(currentPage + 1)}>다음</button
  >
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
    border: 1px solid #555; /* 어두운 테두리 */
    background-color: #333; /* 어두운 배경색 */
    color: white; /* 텍스트 색상 흰색 */
    cursor: pointer;
    transition:
      background-color 0.3s,
      border-color 0.3s;
  }

  .pagination-button:hover {
    background-color: #444; /* 호버 시 배경색 */
    border-color: #666; /* 호버 시 테두리 색 */
  }

  .pagination-button:disabled {
    color: #999; /* 비활성화 텍스트 색 */
    cursor: not-allowed;
  }

  .pagination-button:disabled:hover {
    background-color: #333; /* 비활성화 상태에서의 배경색 */
    border-color: #555; /* 비활성화 상태에서의 테두리 색 */
  }

  .pagination-button.active {
    background-color: #555; /* 활성화된 버튼 배경색 */
    border-color: #777; /* 활성화된 버튼 테두리 색 */
  }
</style>
