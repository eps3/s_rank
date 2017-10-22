<template>
  <div id="rightCard">
    <br/>
    <el-card class="box-card" v-if="!loginFlag">
      <div slot="header">
        <span>你好,请登录</span>
      </div>
      <div>
        <router-link to="/login"><el-button type="primary">登录</el-button></router-link>
        <router-link to="/register"><el-button>注册</el-button></router-link>
      </div>
    </el-card>
    <el-card class="box-card" v-else>
      <div slot="header">
        <span>你好, {{ user.username }}</span>
        <el-button @click="getLogout()" style="float: right;" type="primary">退出</el-button>
      </div>
      <div>
        <p>今天的心情好吗</p>
      </div>
    </el-card>
    <br/>
    <el-card class="box-card">
      <div slot="header">
        <span>Rinking</span>
      </div>
      <div>
        <p>我是Rinking，一个打卡排行榜网站</p>
      </div>
    </el-card>
    <br/>
    <el-card class="box-card">
      <div slot="header">
        <span>通告</span>
      </div>
      <div>
        <p>我们都是共产主义的接班人</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { Base64 } from 'js-base64';
import api from '../api'

export default {
  name: "rightCard",
  data() {
    return {
      loginFlag: api.isLogin(),
      user: {
        username: ""
      }
    };
  },
  methods: {
    getLogout: api.logout
  },
  mounted() {
    if (this.loginFlag) {
      this.user.username = api.getJwtData().data.username
    }
  }
};
</script>
