/**
 * 后端注册中心的命名空间
 */
const state = {
    namespaceName: ''
}

const mutations = {
    WRITE_NAMESPACE_NAME(state, namespaceName) {
        state.namespaceName = namespaceName;
    }
}

export default {
    namespaced: true,
    state,
    mutations
}
