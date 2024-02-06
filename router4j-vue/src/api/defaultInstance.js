import request from '@/utils/request'
export function APIFindAllInstance(query) {
  return request({
    url: '/instance/findAllInstance',
    method: 'get',
    params: query
  })
}

export function APIFindDefaultInstancePage(query) {
  return request({
    url: '/instance/findDefaultInstancePage',
    method: 'get',
    params: query
  })
}

export function APISetupDefaultInstance(data) {
  return request({
    url: '/instance/setupDefaultInstance',
    method: 'post',
    data
  })
}
