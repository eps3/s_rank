<template>
  <div id="headMenu">
    <el-menu theme="dark"  router="router" class="el-menu-demo" mode="horizontal">
      <el-menu-item index="/">主页</el-menu-item>
      <el-menu-item index="/rank">排行榜</el-menu-item>
      <el-menu-item index="/card">打卡</el-menu-item>
      <el-menu-item index="/topic">主题</el-menu-item>
      <el-menu-item  v-if="adminFlag" index="/topic/add">新建主题</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import api from "../api";
export default {
  name: "hello",
  data() {
    return {
      adminFlag:  api.isAdmin()
    };
  },
  methods: {}
};
</script>
