<script>
  import { createEventDispatcher } from 'svelte';

  const dispatch = createEventDispatcher();

  function close() {
      dispatch('close');
  }
</script>

<style>
  .modal-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.7);
      display: flex;
      justify-content: center;
      align-items: center;
      z-index: 1000;
  }
  
  .modal {
      background-color: #fff;
      padding: 20px;
      border-radius: 8px;
      max-width: 400px;
      text-align: center;
  }

  button {
      margin-top: 20px;
  }
</style>

<div class="modal-overlay">
  <div class="modal">
      <h2>개발 중입니다</h2>
      <p>해당 페이지는 현재 개발 중입니다. 죄송합니다.</p>
      <button on:click={close}>확인</button>
  </div>
</div>
