package com.example.park.common;


import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN(1,"ADMIN"),
    USER(0,"USER"),
    GUEST(99,"GUEST");

    private Integer roleCode;
    private String roleName;

    UserRole(Integer roleCode,String roleName){
        this.roleCode=roleCode;
        this.roleName=roleName;
    }

    public static UserRole fromCode(Integer roleCode){
        for(var role : values()){
            if (role.getRoleCode().equals(roleCode)){
                return role;
            }
        }
        return GUEST;
    }
}
