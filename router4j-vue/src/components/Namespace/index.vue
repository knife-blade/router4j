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
import {findNamespaceExist, findAllNamespaces} from "@/api/application";

export default {
  name: "Namespace",

  data() {
    return {
      namespaceExist: false,
      namespaceNameList: [],
      namespaceName: ''
    }
  },

  computed: {
    namespaceName() {
      return this.$store.state.namespace.namespaceName;
    }
  },

  methods: {
    checkNamespaceExist() {
      findNamespaceExist().then(response => {
        this.namespaceExist = response.data;
      })
    },
    findAllNamespaces() {
      findAllNamespaces().then(response => {
        this.namespaceNameList = response.data;
      })
    },
    updateActiveNamespace() {
      this.$store.commit('namespace/WRITE_NAMESPACE_NAME', this.namespaceName);
    },
  },
  created() {
    this.checkNamespaceExist();
    this.findAllNamespaces();
  },
}
</script>

<style scoped>

</style>
