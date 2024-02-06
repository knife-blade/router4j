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

export function findNamespaceExist(query) {
  return request({
    url: '/application/checkNamespaceExist',
    method: 'get',
    params: query
  })
}

export function findAllNamespaces(query) {
  return request({
    url: '/application/findAllNamespaces',
    method: 'get',
    params: query
  })
}
