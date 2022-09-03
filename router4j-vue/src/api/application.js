import request from '@/utils/request'

export function findApplicationNames(query) {
  return request({
    url: '/application/findAllApplication',
    method: 'get',
    params: query
  })
}

export function findInstanceAddresses(query) {
  return request({
    url: '/application/findInstance',
    method: 'get',
    params: query
  })
}
