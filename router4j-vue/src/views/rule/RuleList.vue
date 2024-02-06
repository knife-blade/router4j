<template>
  <div class="app-container">
    <div class="filter-container">
      <el-card>
        <el-form :inline="true">
          <el-form-item label="应用名字">
            <el-select v-model="pageQuery.applicationName" placeholder="输入或选择"
                       filterable allow-create clearable style="width: 150px" class="filter-item"
                       @change="findInstanceAddressesForPage">
              <el-option v-for="item in pageResultList.applicationNames" :key="item" :label="item" :value="item"/>
            </el-select>
          </el-form-item>

          <el-form-item label="实例地址">
            <el-select v-model="pageQuery.instanceAddress" placeholder="输入或选择"
                       filterable allow-create clearable style="width: 200px" class="filter-item">
              <el-option v-for="item in pageResultList.instanceAddresses" :key="item" :label="item" :value="item"/>
            </el-select>
          </el-form-item>

          <el-form-item label="路径">
            <el-input v-model="pageQuery.pathPattern" placeholder="输入" style="width: 300px" class="filter-item">
            </el-input>
          </el-form-item>

          <div class="filter-button-group">
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="findData">
              搜索
            </el-button>

            <el-button v-waves class="filter-item" icon="el-icon-delete" @click="clearSearch">
              重置
            </el-button>
          </div>
        </el-form>

      </el-card>
    </div>

    <el-card>
      <el-row class="operator-button-row">
        <el-button type="primary" @click="handleCreate()">
          新建
        </el-button>

        <el-button type="danger" @click="deleteDataAccurateBatch()">
          删除
        </el-button>
      </el-row>

      <el-table
          :key="tableKey"
          :data="list"
          border
          fit
          highlight-current-row
          @selection-change="handleSelectionChange"
          :header-cell-style="{background:'#F8F8F8'}"
          style="width: 100%;"
      >

        <el-table-column
            type="selection"
            width="55">
        </el-table-column>

        <el-table-column label="应用名字" align="center" min-width="150px" max-wi>
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

            <el-button size="mini" type="danger" @click="deleteDataAccurate(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <pagination v-show="total>0" :total="total" :page.sync="pageQuery.pageNo" :limit.sync="pageQuery.limit"
                @pagination="getPage" :limit="10"/>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dialogData" label-position="left" label-width="70px"
               style="width: 400px; margin-left:50px;">

        <el-form-item label="应用名">
          <el-select v-model="dialogData.applicationName" filterable allow-create
                     class="filter-item" placeholder="输入或选择" @change="findInstanceAddressesForDialog">
            <el-option v-for="item in dialogResultList.applicationNames" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item label="实例地址">
          <el-select v-model="dialogData.instanceAddress" filterable allow-create
                     class="filter-item" placeholder="输入或选择">
            <el-option v-for="item in dialogResultList.instanceAddresses" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item label="路径">
          <el-input v-model="dialogData.pathPattern" placeholder="输入" style="width: 300px" class="filter-item">
          </el-input>
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
import {
  APIPage,
  APIAdd,
  APIEdit,
  APIDeleteAccurate,
  APIDeleteAccurateBatch,
  APIDeleteFuzzy,
  APIFindApplicationNames,
  APIFindInstanceAddresses
} from '@/api/rule'
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
      listMultipleSelection: [],
      total: 0,
      pageQuery: {
        pageNo: 0,
        pageSize: 10,
        applicationName: undefined,
        instanceAddress: undefined,
        pathPattern: undefined,
        // sort: '+id'
      },
      pageResultList: {
        applicationNames: null,
        instanceAddresses: null,
      },
      dialogData: {
        applicationName: '',
        instanceAddress: '',
        pathPattern: ''
      },
      dialogResultList: {
        applicationNames: null,
        instanceAddresses: null,
      },
      dialogOldData: null,
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
    this.findData()
  },

  methods: {
    handleSelectionChange(val) {
      this.listMultipleSelection = val;
    },

    getPage() {
      APIPage(this.pageQuery).then(response => {
        this.list = response.data.dataList
        this.total = response.data.total
      })
    },
    findData() {
      this.getPage()
      this.findAllApplicationNames()
    },

    clearSearch() {
      this.pageQuery.applicationName = undefined
      this.pageQuery.instanceAddress = undefined
      this.pageQuery.pathPattern = undefined
    },
    resetTemp() {
      this.dialogData = {
        applicationName: '',
        instanceAddress: '',
        pathPattern: ''
      }
    },

    findAllApplicationNames() {
      APIFindApplicationNames(null).then((response) => {
        this.pageResultList.applicationNames = response.data
        this.dialogResultList.applicationNames = response.data
      })
    },

    findInstanceAddressesForPage() {
      let query = {
        applicationName: this.pageQuery.applicationName
      }
      APIFindInstanceAddresses(query).then((response) => {
        this.pageResultList.instanceAddresses = response.data
      })
    },

    findInstanceAddressesForDialog() {
      let query = {
        applicationName: this.dialogData.applicationName
      }
      APIFindInstanceAddresses(query).then((response) => {
        this.dialogResultList.instanceAddresses = response.data
      })
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
          APIAdd(this.dialogData).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.findData()
          })
        }
      })
    },

    handleUpdate(row) {
      this.dialogData = Object.assign({}, row) // copy obj
      this.dialogOldData = Object.assign({}, row) // copy obj
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
          let requestBody = {
            oldApplicationName: this.dialogOldData.applicationName,
            oldInstanceAddress: this.dialogOldData.instanceAddress,
            oldPathPattern: this.dialogOldData.pathPattern,
            newApplicationName: this.dialogData.applicationName,
            newInstanceAddress: this.dialogData.instanceAddress,
            newPathPattern: this.dialogData.pathPattern,
          }
          APIEdit(requestBody).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '编辑成功',
              type: 'success',
              duration: 2000
            })
            this.findData()
          })
        }
      })
    },

    deleteFuzzy() {
      APIDeleteFuzzy(this.pageQuery).then(() => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        this.findData()
      })
    },

    deleteDataAccurate(row, index) {
      APIDeleteAccurate(row).then(() => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        this.findData()
      })
    },

    deleteDataAccurateBatch() {
      APIDeleteAccurateBatch(this.listMultipleSelection).then(() => {
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
        this.findData()
      })
    },
  }
}
</script>
<style lang="scss" scoped>

</style>
