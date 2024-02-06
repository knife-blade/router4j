import request from '@/utils/request'

export function APIPage(query) {
  return request({
    url: '/rule/page',
    method: 'get',
    params: query
  })
}

export function APIAdd(data) {
  return request({
    url: '/rule/add',
    method: 'post',
    data
  })
}

export function APIEdit(data) {
  return request({
    url: '/rule/edit',
    method: 'post',
    data
  })
}

export function APIDeleteAccurate(data) {
  return request({
    url: '/rule/deleteAccurate',
    method: 'post',
    data
  })
}

export function APIDeleteAccurateBatch(data) {
  return request({
    url: '/rule/deleteAccurateBatch',
    method: 'post',
    data
  })
}

export function APIDeleteFuzzy(data) {
  return request({
    url: '/rule/deleteFuzzy',
    method: 'post',
    data
  })
}

export function APIFindApplicationNames(query) {
  return request({
    url: '/rule/findApplicationNames',
    method: 'get',
    params: query
  })
}

export function APIFindInstanceAddresses(query) {
  return request({
    url: '/rule/findInstanceAddresses',
    method: 'get',
    params: query
  })
}
