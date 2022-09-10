import request from '@/utils/request'
export function findAllInstance(query) {
  return request({
    url: '/instance/findAllInstance',
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

export function setupDefaultInstance(data) {
  return request({
    url: '/instance/setupDefaultInstance',
    method: 'post',
    data
  })
}
