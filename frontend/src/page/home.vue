<template>
  <div id="Home">
    <head-menu></head-menu>
    <el-row :gutter="20" type="flex">
      <el-col :span="18">
        <br/>
        <br/>
        <el-row class="flex">
          <el-col :span="20">
            <el-pagination
              @current-change="getCardList"
              :current-page.sync="currentPage"
              :page-size="10"
              layout="total, prev, pager, next"
              :total="totalCard">
            </el-pagination>
          </el-col>
          <el-col :span="4">
            <el-input @change="getCardList" v-model="search" placeholder="搜索" icon="search" style="float: right;"></el-input>
          </el-col>
        </el-row>
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
      currentPage: 1,
      cardList: [],
      search: "",
      totalCard: 0
    };
  },
  components: {
    headMenu,
    rightCard
  },
  methods: {
    getCardList() {
      api
        .getCardList(this.currentPage-1, 10, this.search)
        .then(response => {
          var status = response.data.status;
          if (status.status_code != 0) {
            Message({
              message: status.status_reason,
              type: "error"
            });
          } else {
            this.cardList = response.data.result.items;
            this.totalCard = response.data.result.total;
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
