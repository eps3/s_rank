<template>
  <div id="addTopic">
    <head-menu></head-menu>
    <el-row :gutter="20" type="flex">
      <el-col :span="18">
        <br/>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span style="line-height: 36px;">新建主题</span>
          </div>
          <el-input v-model="topicName" placeholder="请输入主题名称"></el-input>
          <br/>
          <br/>
          <el-input v-model="topicDesc"
            type="textarea"
            :rows="5"
            placeholder="请输主题描述">
          </el-input>
          <br/>
          <br/>
          <br/>
          <el-button @click="postTopic" type="primary" style="float: right;">提交</el-button>
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
import api from "../api";

export default {
  name: "addTopic",
  data() {
    return {
      topicName: "",
      topicDesc: ""
    };
  },
  components: {
    headMenu,
    rightCard
  },
  methods: {
    postTopic() {
      api.addTopic({
        name: this.topicName,
        desc: this.topicDesc
      }).then(response => {
          var status = response.data.status;
          if (status.status_code != 0) {
            Message({
              message: status.status_reason,
              type: "error"
            });
          } else {
            Notification({
              title: "添加主题成功",
              message: "开始打卡吧！",
              type: "success"
            });
            this.$router.push("/topic");
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
