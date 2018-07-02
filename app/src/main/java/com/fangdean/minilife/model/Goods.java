package com.fangdean.minilife.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "goods", primaryKeys = {"id", "user_id"})
public class Goods implements Parcelable {
    @NonNull
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "category_id")
    private Long categoryId;

    @NonNull
    @ColumnInfo(name = "user_id")
    private Long userId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "state")
    private Integer state;

    @ColumnInfo(name = "order_num")
    private Integer orderNum;

    @ColumnInfo(name = "update_time")
    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.categoryId);
        dest.writeValue(this.userId);
        dest.writeString(this.name);
        dest.writeValue(this.state);
        dest.writeValue(this.orderNum);
        dest.writeValue(this.updateTime);
    }

    public Goods() {
    }

    protected Goods(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.categoryId = (Long) in.readValue(Long.class.getClassLoader());
        this.userId = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.state = (Integer) in.readValue(Integer.class.getClassLoader());
        this.orderNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.updateTime = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Goods> CREATOR = new Parcelable.Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel source) {
            return new Goods(source);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
}
