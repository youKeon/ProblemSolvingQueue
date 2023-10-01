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
    background-color: white;
  }

  .list-table th,
  .list-table td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: center;
  }

  .list-table td {
    height: 24px;
  }

  .list-table th {
    background-color: #f4f4f4;
    font-weight: bold;
  }

  .list-table td {
    background-color: #fff;
  }

  .list-table tr:nth-child(even) {
    background-color: #f9f9f9;
  }
</style>
