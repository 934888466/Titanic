package com.warehouse.bean;

public class WarehouseGood {
    private Integer id;

    private Integer warehouseId;

    private Integer goodId;

    private Integer number;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "WarehouseGood{" +
                "id=" + id +
                ", warehouseId=" + warehouseId +
                ", goodId=" + goodId +
                ", number=" + number +
                '}';
    }
}