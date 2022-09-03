import request from '@/utils/request'

export function fetchPage(query) {
  return request({
    url: '/rule/page',
    method: 'get',
    params: query
  })
}

export function add(data) {
  return request({
    url: '/rule/add',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/rule/edit',
    method: 'post',
    data
  })
}

export function deleteAccurate(data) {
  return request({
    url: '/rule/deleteAccurate',
    method: 'post',
    data
  })
}

export function deleteAccurateBatch(data) {
  return request({
    url: '/rule/deleteAccurateBatch',
    method: 'post',
    data
  })
}

export function deleteFuzzy(data) {
  return request({
    url: '/rule/deleteFuzzy',
    method: 'post',
    data
  })
}

export function findApplicationNames(query) {
  return request({
    url: '/rule/findApplicationNames',
    method: 'get',
    params: query
  })
}

export function findInstanceAddresses(query) {
  return request({
    url: '/rule/findInstanceAddresses',
    method: 'get',
    params: query
  })
}
