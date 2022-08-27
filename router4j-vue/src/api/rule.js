import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/rule/find',
    method: 'get',
    params: query
  })
}
