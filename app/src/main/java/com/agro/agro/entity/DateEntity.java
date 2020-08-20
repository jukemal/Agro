package com.agro.agro.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "date_entity")
public class DateEntity {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "date")
    public Date date;

    public DateEntity(int uid, Date date) {
        this.uid = uid;
        this.date = date;
    }

    @Override
    public String toString() {
        return "DateEntity{" +
                "uid=" + uid +
                ", date=" + date +
                '}';
    }
}
