package com.b3al.spring.jwt.mongodb.models;

import lombok.Data;

public enum ERole {
  ROLE_NEW_USER,
  ROLE_CLIENT,
  ROLE_PROVIDER,
  ROLE_ADMIN,
  ROLE_SUPER_ADMIN;

  public static ERole fromString(String roleName) {
    for (ERole role : ERole.values()) {
      if (role.name().equals(roleName)) {
        return role;
      }
    }
    return null;
  }
}
