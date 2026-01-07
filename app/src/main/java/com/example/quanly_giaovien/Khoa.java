package com.example.quanly_giaovien;

public class Khoa {
    //public String getMaKhoa;
    String maKhoa;
    String tenKhoa;

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getTenKhoa() {
        return tenKhoa;
    }

    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }
    //Override để spinner hiển thị tên
    @Override
    public String toString() {
        return tenKhoa;
    }
}
