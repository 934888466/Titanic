package com.warehouse.bean;

public class Assess {
    @Override
    public String toString() {
        return "Assess{" +
                "id=" + id +
                ", aname='" + aname + '\'' +
                '}';
    }

    private Integer id;

    private String aname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname == null ? null : aname.trim();
    }
}