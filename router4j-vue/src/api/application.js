import request from '@/utils/request'

export function APIFindApplicationNames(query) {
  return request({
    url: '/application/findAllApplication',
    method: 'get',
    params: query
  })
}

export function APIInstanceAddresses(query) {
  return request({
    url: '/application/findInstance',
    method: 'get',
    params: query
  })
}

export function APIFindNamespaceExist(query) {
  return request({
    url: '/application/checkNamespaceExist',
    method: 'get',
    params: query
  })
}

export function APIFindAllNamespaces(query) {
  return request({
    url: '/application/findAllNamespaces',
    method: 'get',
    params: query
  })
}
