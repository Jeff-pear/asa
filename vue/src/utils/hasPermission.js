import store from '../store'

export function hasPermission(permission) {
  let myPermissions = store.getters.permissions;
  return myPermissions.indexOf(permission) > -1;
}

export function getGroupTag() {
  let groupTag = store.getters.groupTag;
  if(groupTag==undefined){
    let roleTag = store.getters.role;
    if(roleTag == '管理员'){
      return -1;
    }else if(roleTag == '学生'){
      return 1;
    }else if(roleTag =='教师'){
      return 2;
    }else if(roleTag =='财务'){
      return 3;
    }
  }
  return groupTag==undefined ? -1: groupTag;
}

export function getPeriod(periodType) {
   return store.getters.period[periodType]
}

export function needResetPwd() {
  return store.getters.needResetPwd
}

