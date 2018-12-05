<template>
  <el-row>
    <el-col :span="17">
      <el-slider
        v-model="grade"
        range
        show-stops
        :format-tooltip="formatTooltip"
        :max="9" @change="changeVal">
      </el-slider>
    </el-col>
    <el-col :span="5" :offset="2">

      <el-tag type="success">
        <template slot-scope="scope">
          <div>
          {{myFromLabel}} -- {{myToLabel}}
          </div>
        </template>
      </el-tag>
    </el-col>
  </el-row>

</template>
<script>
    export default {
        name: "SliderWithLabels",
        props:['dataVal'],
        data() {
          return {
            grade: this.$props['dataVal'],
            myFromLabel:this.getLabel(0),
            myToLabel:this.getLabel(1),
          }
        },
        methods:{
          changeVal(val){
            debugger;
            this.myFromLabel = this.formatTooltip(val[0]);
            this.myToLabel = this.formatTooltip(val[1]);
          },
          getLabel(index){
            return this.formatTooltip(this.dataVal[index]);
          },
          formatTooltip(val) {
            let resultVal = '';
            if(val == 0){
              resultVal = 'KG'
            }else{
              resultVal = 'G'+String(val);
            }
            return resultVal;
          },
        }
    }
</script>

<style scoped>

</style>
