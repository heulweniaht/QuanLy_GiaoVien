package com.example.quanly_giaovien;
import retrofit2.Call;
import retrofit2.http.*;


import java.util.List;

public interface ApiService {
    String Base_Url = "https://nguyenha-001-site1.ltempurl.com/";

    @GET("api/Khoa")
    Call<List<Khoa>> getListKhoa();

    @GET("api/GiaoVien")
    Call<List<GiaoVien>> getListGiaoVien(@Query("maKhoa") String maKhoa);

    @POST("api/GiaoVien")
    Call<Void> addGiaoVien(@Body GiaoVien giaoVien);

    @PUT("api/GiaoVien/{id}")
    Call<Void> updateGiaoVien(
            @Path("id") String maGV,
            @Body GiaoVien giaoVien
    );

    @DELETE("api/GiaoVien/{id}")
    Call<Void> deleteGiaoVien(@Path("id") String maGV);
}
