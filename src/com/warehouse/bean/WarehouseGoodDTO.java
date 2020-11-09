package com.warehouse.bean;

public class WarehouseGoodDTO extends WarehouseGood {
    private String name;
    private Integer parentId;
    private Integer price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "WarehouseGoods{" +
                "goodName='" + name + '\'' +
                ", parentId=" + parentId +
                ", price=" + price +
                ", id = " + super.getId() +
                ", warehouseId = " + super.getWarehouseId() +
                ", goodId =" + super.getGoodId() +
                ", number= " + super.getNumber() +
                '}';


    }
}
