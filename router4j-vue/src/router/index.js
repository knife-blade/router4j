import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Layout from '@/layout'

const routes = [
    {
        path: '/',
        component: Layout,
        redirect: '/home',
        children: [
            {
                path: 'home',
                component: () => import('@/views/Home'),
                name: 'Dashboard',
                meta: {title: 'Home', icon: 'home', affix: true}
            }
        ]
    },

]

const router = new VueRouter({
    routes
})

export default router
