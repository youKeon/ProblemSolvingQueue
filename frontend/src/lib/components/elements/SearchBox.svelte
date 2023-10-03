<script>
  export let placeholder = "제목 입력";
</script>

<input class="search-box" placeholder={placeholder} />

<style>
  .search-box {
    height: 48px;
    width: 95%;
    margin-bottom: 16px;
    font-size: 1rem;
    padding: 8px 16px;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    transition:
      border-color 0.2s,
      box-shadow 0.2s;

    &:focus {
      border-color: #aaa;
      box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
    }
  }
</style>
