import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Layout from '@/layout'

const routes = [
    {
        path: '/',
        component: Layout,
        redirect: '/home1',
        children: [
            {
                path: 'home1',
                component: () => import('@/views/Home'),
                name: 'Home1',
                meta: {title: '第一页', icon: 'home', affix: true}
            }
        ]
    },

]

const router = new VueRouter({
    routes
})

export default router
