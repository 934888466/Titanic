package com.warehouse.bean;

public class StaffRole {
    private Integer id;

    private Integer staffId;

    private Integer roleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "StaffRole{" +
                "id=" + id +
                ", staffId=" + staffId +
                ", roleId=" + roleId +
                '}';
    }
}