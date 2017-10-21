<template>
  <div id="Register">
    <head-menu></head-menu>
    <el-row :gutter="20" type="flex">
      <el-col :span="18">
        <br/>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span style="line-height: 36px;">用户注册</span>
          </div>
          <div>
             <el-input v-model="username" placeholder="请输入用户名"></el-input>
             <br/>
             <br/>
             <el-input v-model="email" type="email" placeholder="请输入邮箱"></el-input>
             <br/>
             <br/>
             <el-input v-model="password" type="password" placeholder="请输入密码"></el-input>
             <br/>
             <br/>
             <el-button @click="postRegister">提交</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <right-card></right-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import headMenu from "../components/headMenu";
import rightCard from "../components/rightCard";
import { Message } from "element-ui";
import { Notification } from "element-ui";
export default {
  name: "Register",
  data() {
    return {
      username: "",
      password: "",
      email: ""
    };
  },
  components: {
    headMenu,
    rightCard
  },
  methods: {
    postRegister() {
      this.$ajax
        .post("/api/register", {
          name: this.username,
          email: this.email,
          password: this.password
        })
        .then(response => {
          var status = response.data.status;
          if (status.status_code != 0) {
            Message({
              message: "注册失败,"+status.status_reason,
              type: "warning"
            });
          } else {
            Message({
              message: "注册成功,开始打卡吧！",
              type: "success"
            });
            this.$router.push("/");
          }
        })
        .catch(function(error) {
          Message({
            message: "系统错误请联系管理员",
            type: "error"
          });
        });
    }
  }
};
</script>
<style>

</style>
