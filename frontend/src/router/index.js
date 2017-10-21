import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/page/Home'
import Topic from '@/page/Topic'
import addTopic from '@/page/addTopic'
import Card from '@/page/Card'
import Rank from '@/page/Rank'
import Login from '@/page/Login'
import Register from '@/page/Register'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/topic',
      name: 'Topic',
      component: Topic
    },
    {
      path: '/topic/add',
      name: 'addTopic',
      component: addTopic
    },
    {
      path: '/card',
      name: 'Card',
      component: Card
    },
    {
      path: '/rank',
      name: 'Rank',
      component: Rank
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    }
  ]
})
