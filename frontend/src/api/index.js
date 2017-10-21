import { router } from '../main'
import axios from 'axios'
import { Message } from "element-ui";
import { Notification } from "element-ui";

export default {

    getJwtData() {
        var arr, reg = new RegExp("(^| )PLAY_SESSION=([^;]*)(;|$)")
        if (arr = document.cookie.match(reg)) {
            return JSON.parse(Base64.decode(arr[2].split('.')[1]))
        } else {
            return null
        }
    },

    isLogin() {
        if (this.getJwtData() != null) {
            return true
        } else {
            return false
        }
    },

    isAdmin(){
        if (!this.isLogin) {
            return false
        } else {
            if (this.getJwtData().data.role === "ADMIN") {
                return true
            } else{
                return false
            }
        }
    },

    logout() {
        axios.get("/api/logout").then(response => {
            var status = response.data.status;
            if (status.status_code != 0) {
                Message({
                    message: "退出失败: " + status.status_reason,
                    type: "warning"
                });
            } else {
                Message({
                    message: "退出成功! 下次再来玩啊！",
                    type: "success"
                });
                this.$router.push('/login')
            }
        }).catch(function (error) {
            Message({
                message: "系统错误请联系管理员",
                type: "error"
            });
        });
    },

    // topic
    getTopicList() {
        return axios.get("/api/topic")
    },

    getFollowList() {
        return axios.get("/api/topic/follow")
    },

    addTopic(topic){
        return axios.post("/api/topic", topic)
    },

    follow(topic_id){
        return axios.get("/api/topic/"+topic_id+"/follow")
    },

    unFollow(topic_id){
        return axios.get("/api/topic/"+topic_id+"/unFollow")
    },

    postCard(card){
        return axios.post("/api/card", card)
    },

    getCardList() {
        return axios.get("/api/card")
    }

}