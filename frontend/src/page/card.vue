<template>
  <div id="Card">
    <head-menu></head-menu>
    <el-row :gutter="20" type="flex">
      <el-col :span="18">
        <br/>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span style="line-height: 36px;">打卡</span>
          </div>
          <el-input
            v-model="content"
            type="textarea"
            :rows="5"
            placeholder="请输入内容">
          </el-input>
          <br/>
          <br/>
          <br/>
          <el-row>
            <el-col :span="20">
              <el-select v-model="value" placeholder="请选择主题">
                <el-option
                  v-for="item in options"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button @click="card" type="primary" style="float: right;">提交</el-button>
            </el-col>
          </el-row>
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
import api from "../api";
export default {
  name: "Card",
  data() {
    return {
      options: [],
      value: "",
      content: ""
    };
  },
  components: {
    headMenu,
    rightCard
  },
  methods: {
    followList() {
      api
        .getFollowList()
        .then(response => {
          var status = response.data.status;
          if (status.status_code != 0) {
            this.$message({
              message: status.status_reason,
              type: "warning"
            });
          } else {
            this.options = response.data.result;
          }
        })
        .catch(function(error) {
          this.$message({
            message: "系统错误，请联系管理员",
            type: "error"
          });
        });
    },
    card() {
      if (api.isLogin()) {
        if (this.value === "") {
          this.$message("请选择主题");
        } else {
          api
            .postCard({
              topic_id: parseInt(this.value),
              content: this.content
            })
            .then(response => {
              var status = response.data.status;
              if (status.status_code != 0) {
                this.$message({
                  message: status.status_reason,
                  type: "warning"
                });
              } else {
                this.$message({
                  message: "打卡成功！",
                  type: "success"
                });
                this.$router.push("/")
              }
            })
            .catch(function(error) {
              this.$message({
                message: "系统错误，请联系管理员",
                type: "error"
              });
            });
        }
      } else {
        this.$message("傻孩子，先去登录吧！");
      }
    }
  },
  mounted() {
    this.followList();
  }
};
</script>
<style>

</style>
