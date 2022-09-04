<template>
  <div class="app-container">
    <div class="filter-container">
      <el-card>
        <el-form :inline="true">
          <el-form-item>
            <label-wrap>应用名字</label-wrap>
            <el-select v-model="pageQuery.applicationName" placeholder="输入或选择"
                       filterable allow-create clearable style="width: 150px" class="filter-item"
            >
              <el-option v-for="item in pageResultList.applicationNames" :key="item" :label="item" :value="item"/>
            </el-select>
          </el-form-item>

          <div class="filter-button">
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="findData">
              搜索
            </el-button>
          </div>
        </el-form>

      </el-card>
    </div>

    <el-card>
      <el-row class="operator-button-row">
        <el-button type="primary" @click="handleCreate()">
          设置默认路由
        </el-button>

        <el-button type="primary" @click="handleCreate()">
          设置强制路由
        </el-button>

        <el-button type="danger" @click="deleteDataAccurateBatch()">
          取消默认路由
        </el-button>

        <el-button type="danger" @click="deleteDataAccurateBatch()">
          取消强制路由
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

        <el-table-column label="运行状态" min-width="100px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.isRunning }}</span>
          </template>
        </el-table-column>

        <el-table-column label="强制路由" min-width="50px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.forceRoute }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="500" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button type="primary" size="mini" @click="handleUpdate(row)">
              设置默认路由
            </el-button>

            <el-button type="primary" size="mini" @click="cancelData(row,$index)">
              设置强制路由
            </el-button>

            <el-button type="danger" size="mini" @click="handleUpdate(row)">
              取消默认路由
            </el-button>

            <el-button type="danger" size="mini" @click="cancelData(row,$index)">
              取消强制路由
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <pagination v-show="total>0" :total="total" :page.sync="pageQuery.page" :limit.sync="pageQuery.limit"
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
          <el-input v-model="dialogData.forceRoute" placeholder="输入" style="width: 300px" class="filter-item">
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
  findApplicationNames,
  findDefaultInstancePage,
  markAsDefaultInstance,
  cancelDefaultInstance
} from '@/api/defaultInstance'
import variables from '@/styles/variables.scss'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'InstanceList',
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
        page: 0,
        size: 10,
        applicationName: undefined,
      },
      pageResultList: {
        applicationNames: null,
        instanceAddresses: null,
      },
      dialogData: {
        applicationName: '',
        instanceAddress: '',
        isRunning: false,
        forceRoute: ''
      },
      dialogResultList: {
        applicationNames: null,
        instanceAddresses: null,
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
        forceRoute: [{required: true, message: '是否强制路由是必填的', trigger: 'change'}]
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
      findDefaultInstancePage(this.pageQuery).then(response => {
        this.list = response.data.dataList
        this.total = response.data.total
      })
    },
    findData() {
      this.getPage()
      this.findAllApplicationNames()
    },
    resetTemp() {
      this.dialogData = {
        applicationName: '',
        instanceAddress: '',
        forceRoute: ''
      }
    },

    findAllApplicationNames() {
      findApplicationNames(null).then((response) => {
        this.pageResultList.applicationNames = response.data
        this.dialogResultList.applicationNames = response.data
      })
    },

    findInstanceAddressesForPage() {
      let query = {
        applicationName: this.pageQuery.applicationName
      }
      findDefaultInstancePage(query).then((response) => {
        this.pageResultList.instanceAddresses = response.data
      })
    },

    findInstanceAddressesForDialog() {
      let query = {
        applicationName: this.dialogData.applicationName
      }
      findDefaultInstancePage(query).then((response) => {
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
          markAsDefaultInstance(this.dialogData).then(() => {
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
          markAsDefaultInstance(this.dialogData).then(() => {
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

    cancelData(row, index) {
      cancelDefaultInstance(row).then(() => {
        this.$notify({
          title: '成功',
          message: '取消成功',
          type: 'success',
          duration: 2000
        })
        this.findData()
      })
    },

    deleteDataAccurateBatch() {
      cancelDefaultInstance(this.listMultipleSelection).then(() => {
        this.$notify({
          title: '成功',
          message: '取消成功',
          type: 'success',
          duration: 2000
        })
        this.findData()
      })
    },
  }
}
</script>
