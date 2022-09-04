import request from '@/utils/request'
export function findApplicationNames(query) {
  return request({
    url: '/instance/findApplicationNames',
    method: 'get',
    params: query
  })
}

export function findDefaultInstancePage(query) {
  return request({
    url: '/instance/findDefaultInstancePage',
    method: 'get',
    params: query
  })
}

export function markAsDefaultInstance(data) {
  return request({
    url: '/instance/markAsDefaultInstance',
    method: 'post',
    data
  })
}

export function cancelDefaultInstance(data) {
  return request({
    url: '/instance/cancelDefaultInstance',
    method: 'post',
    data
  })
}
