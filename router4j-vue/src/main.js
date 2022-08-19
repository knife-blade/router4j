import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import './icons' // icon
import ElementUI  from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';

import '@/styles/index.scss' // global css


Vue.use(ElementUI);
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
