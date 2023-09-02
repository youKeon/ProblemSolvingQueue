<!-- 여기는 문제 리스트 페이지입니다. -->
<script lang="ts">
  import { getProblemsList } from "../../api/api";

  async function fetchProblems(){
    const pageableData = {
      page: 0,
      size: 10,
      sort: ["level,"]
    };

    try{
      const problems = await getProblemsList(pageableData);
      console.log('Fetched problems: ', problems);
    } catch (error){
      console.log("문제 목록 조회 중 오류 발생:", error);
    }
  }

  fetchProblems();
</script>
<h1>여기는 problems 페이지입니다.</h1>