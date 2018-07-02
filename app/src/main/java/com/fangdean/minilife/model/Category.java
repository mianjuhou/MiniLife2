package com.fangdean.minilife.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "category",primaryKeys = {"id","user_id"})
public class Category implements Parcelable {
    @NonNull
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "parent_id")
    private Long parentId;

    @NonNull
    @ColumnInfo(name = "user_id")
    private Long userId;

    @ColumnInfo(name = "name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        dest.writeString(this.name);
        dest.writeValue(this.parentId);
        dest.writeValue(this.userId);
        dest.writeValue(this.orderNum);
        dest.writeValue(this.updateTime);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.parentId = (Long) in.readValue(Long.class.getClassLoader());
        this.userId = (Long) in.readValue(Long.class.getClassLoader());
        this.orderNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.updateTime = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
