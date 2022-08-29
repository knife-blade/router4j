import request from '@/utils/request'

export function fetchPage(query) {
  return request({
    url: '/rule/find',
    method: 'get',
    params: query
  })
}
