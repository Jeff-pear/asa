import store from '../store'

export function hasPermission(permission) {
  let myPermissions = store.getters.permissions;
  return myPermissions.indexOf(permission) > -1;
}

export function getGroupTag() {
  let groupTag = store.getters.groupTag;
  return groupTag;
}

export function getPeriod(periodType) {
   return store.getters.period[periodType]
}

