<template>
  <div id="Home">
    <head-menu></head-menu>
    <el-row :gutter="20" type="flex">
      <el-col :span="18">
        <br/>
        <el-row :gutter="15">
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span style="line-height: 36px;">开发中</span>
                <el-button style="float: right;" type="primary">操作按钮</el-button>
              </div>
              <div v-for="o in 4" :key="o" class="text item">
                {{'开发中 ' + o }}
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span style="line-height: 36px;">开发中</span>
                <el-button style="float: right;" type="primary">操作按钮</el-button>
              </div>
              <div v-for="o in 4" :key="o" class="text item">
                {{'开发中 ' + o }}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="6">
        <right-card></right-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import headMenu from '../components/headMenu'
  import rightCard from '../components/rightCard'
  export default {
    name: 'Rank',
    data() {
      return {
      };
    },
    components: {
      headMenu,
      rightCard
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>
<style>
</style>
