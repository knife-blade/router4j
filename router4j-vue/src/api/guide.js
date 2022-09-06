import request from '@/utils/request'

export function saveGuide(query) {
  return request({
    url: '/guide/save',
    method: 'get',
    params: query
  })
}

