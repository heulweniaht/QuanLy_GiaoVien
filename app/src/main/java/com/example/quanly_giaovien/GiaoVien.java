package com.example.quanly_giaovien;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class GiaoVien implements Serializable {

    // Sửa lỗi Crash ở đây: Đảm bảo không trùng lặp tên trong value và alternate
    @SerializedName(value = "hoten", alternate = {"tenGV", "hoTenGV", "name", "fullName"})
    private String hoTen;

    private String maGV;
    private String hocHam;
    private String hocVi;
    private String email;

    @SerializedName(value = "sdt", alternate = {"soDienThoai", "phone"})
    private String soDienThoai;
    private String anh;
    private String maKhoa;
    private String tenKhoa;

    public GiaoVien() {}

    // Getter & Setter
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getHocHam() { return hocHam; }
    public void setHocHam(String hocHam) { this.hocHam = hocHam; }

    public String getHocVi() { return hocVi; }
    public void setHocVi(String hocVi) { this.hocVi = hocVi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getAnh() { return anh; }
    public void setAnh(String anh) { this.anh = anh; }

    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }

    public String getTenKhoa() { return tenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }
}