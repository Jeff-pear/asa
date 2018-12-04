import store from '../store'

export function hasPermission(permission) {
  let myPermissions = store.getters.permissions;
  return myPermissions.indexOf(permission) > -1;
}

export function isAdmin(roleName) {
  let myRole = store.getters.role;
  return myRole.indexOf(roleName) > -1;
}

