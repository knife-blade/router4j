<template>
  <div class="app-container">
    <div class="filter-container">
      <el-card>
        <el-select v-model="pageQuery.applicationName" placeholder="输入或选择"
                   allow-create clearable style="width: 200px" class="filter-item">
          <el-option v-for="item in applicationNames" :key="item" :label="item" :value="item"/>
        </el-select>

        <el-select v-model="pageQuery.instanceAddress" placeholder="输入或选择"
                   allow-create clearable style="width: 200px" class="filter-item">
          <el-option v-for="item in instanceAddresses" :key="item" :label="item" :value="item"/>
        </el-select>

        <el-select v-model="pageQuery.pathPattern" placeholder="输入或选择"
                   allow-create clearable style="width: 300px" class="filter-item">
          <el-option v-for="item in pathPatterns" :key="item" :label="item" :value="item"/>
        </el-select>

        <div class="filter-button">
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">
            搜索
          </el-button>

          <el-button v-waves class="filter-item" type="danger" icon="el-icon-delete" @click="handleDelete">
            删除
          </el-button>
        </div>
      </el-card>
    </div>

    <el-card>
      <el-row class="operator-button-row">
        <el-button type="primary" @click="handleCreate()">
          新建
        </el-button>

        <el-button type="danger" @click="handleDelete()">
          删除
        </el-button>
      </el-row>

      <el-table
          :key="tableKey"
          v-loading="listLoading"
          :data="list"
          border
          fit
          highlight-current-row
          :header-cell-style="{background:'#F8F8F8'}"
          style="width: 100%;"
      >

        <el-table-column
            type="selection"
            width="55">
        </el-table-column>

        <el-table-column label="应用名" align="center" min-width="150px" max-wi>
          <template slot-scope="{row}">
            <span>{{ row.applicationName }}</span>
          </template>

        </el-table-column>

        <el-table-column label="实例地址" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.instanceAddress }}</span>
          </template>
        </el-table-column>

        <el-table-column label="路径" min-width="200px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.pathPattern }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleUpdate(row)">
              编辑
            </el-button>

            <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <pagination v-show="total>0" :total="total" :page.sync="pageQuery.page" :limit.sync="pageQuery.limit"
                @pagination="getPage"/>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dialogData" label-position="left" label-width="70px"
               style="width: 400px; margin-left:50px;">

        <el-form-item label="应用名">
          <el-select v-model="dialogData.applicationName" allow-create class="filter-item" placeholder="输入或选择">
            <el-option v-for="item in applicationNames" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item label="实例地址">
          <el-select v-model="dialogData.instanceAddress" allow-create class="filter-item" placeholder="输入或选择">
            <el-option v-for="item in instanceAddresses" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item label="路径">
          <el-select v-model="dialogData.pathPattern" allow-create class="filter-item" placeholder="输入或选择">
            <el-option v-for="item in pathPatterns" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {fetchPage} from '@/api/rule'
import variables from '@/styles/variables.scss'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'RuleList',
  components: {Pagination},
  directives: {waves},
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageQuery: {
        page: 0,
        size: 10,
        applicationName: undefined,
        instanceAddress: undefined,
        pathPattern: undefined,
        // sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      applicationNames: ['user', 'product'],
      instanceAddresses: ['published', 'draft', 'deleted'],
      pathPatterns: ['/user/add', '/user/edit'],
      dialogData: {
        applicationName: '',
        instanceAddress: '',
        pathPattern: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      dialogPvVisible: false,
      rules: {
        applicationName: [{required: true, message: '应用名是必填的', trigger: 'change'}],
        instanceAddress: [{required: true, message: '实例地址是必填的', trigger: 'change'}],
        pathPattern: [{required: true, message: '路径是必填的', trigger: 'change'}]
      },
      variables() {
        return variables
      },
    }
  },
  created() {
    this.getPage()
  },

  methods: {
    getPage() {
      this.listLoading = true
      fetchPage(this.pageQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFind() {
      this.pageQuery.page = 0
      this.getPage()
    },
    resetTemp() {
      this.dialogData = {
        applicationName: '',
        instanceAddress: '',
        pathPattern: ''
      }
    },

    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.dialogData.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.dialogData.author = 'vue-element-admin'
          // createArticle(this.dialogData).then(() => {
          //   this.list.unshift(this.dialogData)
          //   this.dialogFormVisible = false
          //   this.$notify({
          //     title: 'Success',
          //     message: 'Created Successfully',
          //     type: 'success',
          //     duration: 2000
          //   })
          // })
        }
      })
    },

    handleUpdate(row) {
      this.dialogData = Object.assign({}, row) // copy obj
      this.dialogData.timestamp = new Date(this.dialogData.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.dialogData)
          // updateArticle(tempData).then(() => {
          //   const index = this.list.findIndex(v => v.id === this.dialogData.id)
          //   this.list.splice(index, 1, this.dialogData)
          //   this.dialogFormVisible = false
          //   this.$notify({
          //     title: 'Success',
          //     message: 'Update Successfully',
          //     type: 'success',
          //     duration: 2000
          //   })
          // })
        }
      })
    },

    handleDelete(row, index) {
      this.$notify({
        title: 'Success',
        message: 'Delete Successfully',
        type: 'success',
        duration: 2000
      })
      this.list.splice(index, 1)
    }
  }
}
</script>
