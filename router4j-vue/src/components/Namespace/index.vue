<template>
  <div v-if="namespaceExist">
    <el-form :inline="true">
      <el-form-item label="命名空间">
        <el-select v-model="namespaceName" placeholder="请选择"
                   filterable clearable style="width: 150px" class="filter-item"
                   @change="updateActiveNamespace"
        >
          <el-option v-for="item in namespaceNameList" :key="item" :label="item" :value="item"/>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {APIFindNamespaceExist, APIFindAllNamespaces} from "@/api/application";

export default {
  name: "Namespace",

  data() {
    return {
      namespaceExist: false,
      namespaceNameList: [],
      namespaceName: ''
    }
  },

  methods: {
    checkNamespaceExist() {
      APIFindNamespaceExist().then(response => {
        this.namespaceExist = response.data;
        if (this.namespaceExist) {
          this.findAllNamespaces();
        }
      })
    },
    findAllNamespaces() {
      APIFindAllNamespaces().then(response => {
        this.namespaceNameList = response.data;
      })
    },
    updateActiveNamespace(namespaceName) {
      console.log("更新:" + namespaceName)
      this.$store.commit('namespace/WRITE_NAMESPACE_NAME', namespaceName);
      console.log("读取：" + this.$store.state.namespace.namespaceName)
    },
  },
  created() {
    this.checkNamespaceExist();
  },
}
</script>

<style scoped>

</style>
