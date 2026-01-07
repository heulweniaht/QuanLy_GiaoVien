package com.example.quanly_giaovien;
import retrofit2.Call;
import retrofit2.http.*;


import java.util.List;

public interface ApiService {
    String Base_Url = "https://nguyenha-001-site1.ltempurl.com/";

    @GET("api/khoa")
    Call<List<Khoa>> getListKhoa();

    @GET("api/giaovien")
    Call<List<GiaoVien>> getListGiaoVien(@Query("maKhoa") String maKhoa);

    @POST("api/giaovien")
    Call<Void> addGiaoVien(@Body GiaoVien giaoVien);

    @PUT("api/giaovien")
    Call<Void> updateGiaoVien(@Body GiaoVien giaoVien);

    @DELETE("api/giaovien/{id}")
    Call<Void> deleteGiaoVien(@Path("id") String maGV);
}
