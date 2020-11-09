package com.warehouse.bean;

public class StaffAssess {
    private Integer id;
    private Integer staffId;
    private Integer assessId;

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

    public Integer getAssessId() {
        return assessId;
    }

    public void setAssessId(Integer assessId) {
        this.assessId = assessId;
    }

    @Override
    public String toString() {
        return "StaffAssess{" +
                "id=" + id +
                ", staffId=" + staffId +
                ", assessId=" + assessId +
                '}';
    }
}
