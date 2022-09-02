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
