import request from '@/utils/request'

export function markAsDefaultInstance(query) {
  return request({
    url: '/instance/markAsDefaultInstance',
    method: 'get',
    params: query
  })
}

export function cancelDefaultInstance(query) {
  return request({
    url: '/instance/cancelDefaultInstance',
    method: 'get',
    params: query
  })
}
