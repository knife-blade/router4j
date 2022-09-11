<template>
  <div class="app-container">
    <div class="filter-container">
      <el-card>
        <el-form :inline="true">
          <el-form-item label="应用名字">
            <el-select v-model="pageQuery.applicationName" placeholder="输入或选择"
                       filterable allow-create clearable style="width: 150px" class="filter-item"
                       @change="findInstanceForHeader">
              <el-option v-for="item in headerResultArray.applicationNames" :key="item" :label="item" :value="item"/>
            </el-select>
          </el-form-item>

          <el-form-item label="实例的IP">
            <el-select v-model="pageQuery.instanceIp" placeholder="输入或选择"
                       filterable allow-create clearable style="width: 150px" class="filter-item"
                       @change="findInstanceForHeader">
              <el-option v-for="item in headerResultArray.instanceIps" :key="item" :label="item" :value="item"/>
            </el-select>
          </el-form-item>

          <el-form-item label="实例的端口">
            <el-select v-model="pageQuery.instancePort" placeholder="输入或选择"
                       filterable allow-create clearable style="width: 150px" class="filter-item"
                       @change="findInstanceForHeader">
              <el-option v-for="item in headerResultArray.instancePorts" :key="item" :label="item" :value="item"/>
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
        <div style="display: inline-block">
          <el-button type="primary" @click="handleCreate()" class="operator-button">
            创建设置
          </el-button>
        </div>

        <div style="display: inline-block" class="operator-button operator-button-group">
          <span style="font-size: 14px; margin-right: 10px">默认路由</span>
          <el-button-group>
            <el-button v-waves type="primary" size="small"
                       @click="setupDefaultInstanceForBatch(true, null)">
              设置
            </el-button>
            <el-button v-waves type="danger" size="small"
                       @click="setupDefaultInstanceForBatch(false, null)">
              取消
            </el-button>
          </el-button-group>

        </div>

        <div style="display: inline-block" class="operator-button operator-button-group">
          <span style="font-size: 14px; margin-right: 10px">强制路由</span>
          <el-button-group>
            <el-button v-waves type="primary" size="small"
                       @click="setupDefaultInstanceForBatch(null, true)">
              设置
            </el-button>
            <el-button v-waves type="danger" size="small"
                       @click="setupDefaultInstanceForBatch(null, false)">
              取消
            </el-button>
          </el-button-group>
        </div>

      </el-row>

      <el-table
          :key="tableKey"
          :data="pageResultList"
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

        <el-table-column label="实例的IP" min-width="150px">
          <template slot-scope="{row}">
            <span>{{ row.instanceIp }}</span>
          </template>
        </el-table-column>

        <el-table-column label="实例的端口" min-width="100px">
          <template slot-scope="{row}">
            <span>{{ row.instancePort }}</span>
          </template>
        </el-table-column>

        <el-table-column label="运行状态" min-width="100px" align="center">
          <template slot-scope="{row}">
            <el-switch v-model="row.isRunning" disabled></el-switch>
          </template>
        </el-table-column>

        <el-table-column label="默认路由" width="150px" align="center">
          <template slot-scope="{row}">
            <el-switch v-model="row.isDefaultInstance" @change="setupDefaultInstance(row)"></el-switch>
          </template>
        </el-table-column>

        <el-table-column label="强制路由" width="150px" align="center">
          <template slot-scope="{row}">
            <el-switch v-model="row.isForceRoute" @change="setupDefaultInstance(row)"></el-switch>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <pagination v-show="total>0" :total="total" :page.sync="pageQuery.page" :limit.sync="pageQuery.limit"
                @pagination="getPage" :limit="10"/>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dialogData" label-position="left" label-width="120px"
               style="width: 400px; margin-left:50px;">

        <el-form-item label="应用名">
          <el-select v-model="dialogData.applicationName" placeholder="输入或选择"
                     filterable allow-create clearable style="width: 150px">
            <el-option v-for="item in dialogResultArray.applicationNames" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item label="实例的IP">
          <el-select v-model="dialogData.instanceIp" placeholder="输入或选择"
                     filterable allow-create clearable style="width: 150px">
            <el-option v-for="item in dialogResultArray.instanceIps" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item label="实例的端口">
          <el-select v-model="dialogData.instancePort" placeholder="输入或选择"
                     filterable allow-create clearable style="width: 150px">
            <el-option v-for="item in dialogResultArray.instancePorts" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item label="设置为默认路由">
          <el-switch v-model="dialogData.isDefaultInstance" @change="modifyIsForceRoute">
          </el-switch>
        </el-form-item>

        <el-form-item label="设置为强制路由">
          <el-switch v-model="dialogData.isForceRoute" @change="modifyIsDefaultInstance">
          </el-switch>

        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary"
                   @click="dialogStatus==='create'?setupDefaultInstanceForDialog():setupDefaultInstanceForDialog()">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  findAllInstance,
  findDefaultInstancePage,
  setupDefaultInstance
} from '@/api/defaultInstance'
import variables from '@/styles/variables.scss'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'InstanceList',
  components: {Pagination},
  directives: {waves},
  data() {
    return {
      tableKey: 0,
      headerResultArray: {
        applicationNames: null,
        instanceIps: null,
        instancePorts: null
      },
      total: 0,
      pageQuery: {
        page: 0,
        size: 10,
        applicationName: undefined,
        instanceIp: undefined,
        instancePort: undefined
      },
      pageResultList: null,
      pageMultipleSelection: [],
      dialogData: {
        applicationName: '',
        instanceIp: '',
        instancePort: null,
        isDefaultInstance: undefined,
        isForceRoute: undefined
      },
      dialogResultArray: {
        applicationNames: null,
        instanceIps: null,
        instancePorts: null,
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      dialogPvVisible: false,
      operatorButtonRow: {
        isDefaultInstance: false,
        isForceRoute: false
      },
      rules: {
        applicationName: [{required: true, message: '应用名是必填的', trigger: 'change'}],
        instanceIp: [{required: true, message: '实例的IP地址是必填的', trigger: 'change'}],
        instancePort: [{required: true, message: '实例的端口号是必填的', trigger: 'change'}],
        isDefaultInstance: [{required: true, message: '是否默认路由是必填的', trigger: 'change'}],
        isForceRoute: [{required: true, message: '是否强制路由是必填的', trigger: 'change'}]
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
      this.pageMultipleSelection = val;
    },

    getPage() {
      findDefaultInstancePage(this.pageQuery).then(response => {
        this.pageResultList = response.data.dataList
        this.total = response.data.total
      })
    },
    findData() {
      this.getPage()
      this.findInstanceForHeader()
      this.findInstanceForDialog()
    },
    resetTemp() {
      this.dialogData = {
        applicationName: '',
        instanceIp: '',
        instancePort: null,
        isDefaultInstance: null,
        isForceRoute: null
      }
    },

    findInstanceForHeader() {
      findAllInstance(this.pageQuery).then((response) => {
        this.headerResultArray.applicationNames = response.data.applicationNameList;
        this.headerResultArray.instanceIps = response.data.instanceIpList;
        this.headerResultArray.instancePorts = response.data.instancePortList;
      })
    },

    findInstanceForDialog() {
      findAllInstance(this.dialogData).then((response) => {
        this.dialogResultArray.applicationNames = response.data.applicationNameList;
        this.dialogResultArray.instanceIps = response.data.instanceIpList;
        this.dialogResultArray.instancePorts = response.data.instancePortList;
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

    setupDefaultInstanceForBatch(isDefaultInstance, isForceRoute) {
      let requestBodyArray = this._.cloneDeep(this.pageMultipleSelection);
      for (let requestBody of requestBodyArray) {
        if (isDefaultInstance === null) {
          requestBody.isForceRoute = isForceRoute;
          if (isForceRoute) {
            requestBody.isDefaultInstance = true;
          }
        }
        if (isForceRoute === null) {
          requestBody.isDefaultInstance = isDefaultInstance;
          if (!isDefaultInstance) {
            requestBody.isForceRoute = false
          }
        }
      }

      setupDefaultInstance(requestBodyArray).then(() => {
        this.dialogFormVisible = false
        this.$notify({
          title: '成功',
          message: '设置成功',
          type: 'success',
          duration: 2000
        })
        this.findData()
      })
    },

    setupDefaultInstance(row) {
      let requestBodyArray = [];
      requestBodyArray.push(row);

      setupDefaultInstance(requestBodyArray).then(() => {
        this.dialogFormVisible = false
        this.$notify({
          title: '成功',
          message: '设置成功',
          type: 'success',
          duration: 2000
        })
        this.findData();
      })
    },

    setupDefaultInstanceForDialog() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          let requestBody = [];
          requestBody.push(this.dialogData);

          setupDefaultInstance(requestBody).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '设置成功',
              type: 'success',
              duration: 2000
            })
            this.findData()
          })
        }
      })
    },

    // 如果不是默认路由，那么也不能设置为强制路由
    modifyIsForceRoute() {
      if (this.dialogData.isDefaultInstance === false) {
        this.dialogData.isForceRoute = false;
      }
    },

    // 如果是强制路由，那么也要设置为默认路由
    modifyIsDefaultInstance() {
      if (this.dialogData.isForceRoute === true) {
        this.dialogData.isDefaultInstance = true;
      }
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
          setupDefaultInstance(this.dialogData).then(() => {
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
    }
  }
}
</script>
