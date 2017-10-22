<template>
  <div id="Home">
    <head-menu></head-menu>
    <el-row :gutter="20" type="flex">
      <el-col :span="18">
        <div v-for="item in cardList">
          <br/>
          <el-card class="box-card">
            <div>
              <el-tag type="primary">{{ item.topic_name }}</el-tag>
              <el-tag type="primary">{{ item.create_time  | formatDate }}</el-tag>
              <el-tag type="success">{{ item.user_name }}</el-tag>
              <p>{{ item.content }}</p>
            </div>
          </el-card>
        </div>
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
import api from "../api";

export default {
  name: "Home",
  data() {
    return {
      cardList: []
    };
  },
  components: {
    headMenu,
    rightCard
  },
  methods: {
    getCardList() {
      api
        .getCardList()
        .then(response => {
          var status = response.data.status;
          if (status.status_code != 0) {
            Message({
              message: status.status_reason,
              type: "error"
            });
          } else {
            this.cardList = response.data.result;
          }
        })
        .catch(function(error) {
          this.$message({
            message: "系统错误，请联系管理员",
            type: "error"
          });
        });
    }
  },
  mounted() {
    this.getCardList();
  }
};
</script>
<style>

</style>
