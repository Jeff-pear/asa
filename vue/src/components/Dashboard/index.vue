<template>

</template>

<script>
    export default {
      name: "dashboard-index",
      created() {
        this.routeTo();
      },
      methods: {
        routeTo(){
          let role = this.getGroupTag();
          if(role == '1'){
            this.$router.push({ name: 'courseStudent'})
          }else if(role =='2'){
            this.$router.push({ name: 'courseTeacher'})
          }else if(role =='3'){
            this.$router.push({ name: 'courseFinance'})
          }

        }
      }
    }
</script>

<style scoped>

</style>
