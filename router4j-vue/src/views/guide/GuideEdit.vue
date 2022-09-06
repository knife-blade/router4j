<template>
  <div class="app-container">
    <el-button type="primary" @click="saveGuideData()">发布</el-button>

    <el-form :model="guideDetail" :rules="rules" ref="dataForm" label-width="100px">
      <el-form-item label="内容" prop="content">
        <mavon-editor v-model="guideDetail.content"></mavon-editor>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {saveGuide} from "@/api/guide";
export default {
  name: "GuideEdit",
  data() {
    return {
      guideDetail: {
        content: ''
      },
      rules: {
        content: [
          {required: true, message: '请输入内容', trigger: 'blur'}
        ]
      },
    }
  },
  methods: {
    saveGuideData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          saveGuide(this.guideDetail.content).then(() => {
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>