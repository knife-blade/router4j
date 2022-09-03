import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Layout from '@/layout'

const routes = [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path(.*)',
                component: () => import('@/views/misc/redirect/index')
            }
        ]
    },
    {
        path: '/',
        component: Layout,
        redirect: '/rule'
    },
    {
        path: '/404',
        component: () => import('@/views/misc/error-page/404'),
        hidden: true
    },
    {
        path: '/rule',
        component: Layout,
        redirect: '/rule/ruleList',
        meta: {title: '规则管理', icon: 'component'},
        children: [
            {
                path: 'ruleList',
                component: () => import('@/views/rule/RuleList'),
                name: 'RuleList',
                meta: {title: '规则列表'}
            }
        ]
    },
    {
        path: '/instance',
        component: Layout,
        redirect: '/instance/instanceList',
        meta: {title: '实例管理', icon: 'component'},
        children: [
            {
                path: 'instanceList',
                component: () => import('@/views/instance/InstanceList'),
                name: 'ApplicationList',
                meta: {title: '实例列表'}
            }
        ]
    },
    {
        path: '/guide',
        component: Layout,
        redirect: '/guide/guideDetail',
        meta: {title: '使用指南', icon: 'component'},
        children: [
            {
                path: 'guideDetail',
                component: () => import('@/views/guide/GuideDetail'),
                name: 'GuideDetail',
                meta: {title: '指南详情'}
            }
        ]
    },

    // 404 page must be placed at the end !!!
    {path: '*', redirect: '/404', hidden: true}
]

const router = new VueRouter({
    routes
})

export default router
