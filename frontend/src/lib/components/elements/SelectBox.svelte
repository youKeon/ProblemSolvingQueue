<script lang="ts">
  export let id: string;
  export let label: string;
  export let options: { value: string; text: string; }[];
</script>

<div class="select-category-item">
  <label for={id} class="visually-hidden">{label}</label>
  <select class="select-box" id={id}>
    {#each options as { value, text }}
      <option value={value}>{text}</option>
    {/each}
  </select>
</div>

<style>
.select-category-item {
  position: relative;
}

.select-box {
  width: 100%;
  padding: 0.5em;
  font-size: 0.8em;
}

.visually-hidden {
  position: absolute;
  width: 1px;
  height: 1px;
  margin: -1px;
  padding: 0;
  overflow: hidden;
  clip: rect(0,0,0,0);
  white-space: nowrap;
  border: 0;
}
</style>
