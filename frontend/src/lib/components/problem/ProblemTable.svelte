<script lang="ts">
  export let problems: any = [];
</script>

<table class="list-table">
  <thead>
    <tr>
      <th>즐겨찾기</th>
      <th>상태</th>
      <th>제목</th>
      <th>난이도</th>
    </tr>
  </thead>
  <tbody>
    {#each problems as { bookmark, status, title, level } (title)}
      <tr>
        <td>{bookmark}</td>
        <td>{status}</td>
        <td>{title}</td>
        <td>{level}</td>
      </tr>
    {/each}
  </tbody>
</table>

<style>
  .list-table {
    width: 100%;
    border-collapse: collapse;
    background-color: #333; /* 어두운 배경색 */
    color: white; /* 글자색 */
  }

  .list-table th,
  .list-table td {
    border: 1px solid #555; /* 어두운 테두리 */
    padding: 8px;
    text-align: center;
  }

  .list-table th {
    background-color: #444; /* 조금 더 어두운 헤더 배경색 */
    font-weight: bold;
  }

  .list-table tr:nth-child(even) {
    background-color: #3a3a3a; /* 어두운 행 배경색 */
  }

  .list-table tr:hover {
    background-color: #555; /* 호버 시 색상 */
  }
</style>
