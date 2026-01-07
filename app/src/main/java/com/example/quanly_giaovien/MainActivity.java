package com.example.quanly_giaovien;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Spinner spnKhoa;
    RecyclerView rcvGv;
    FloatingActionButton fabAdd;

    GiaoVienAdapter adapter;
    ApiService apiService;

    List<GiaoVien> allList = new ArrayList<>();      // Danh sách gốc
    List<GiaoVien> filteredList = new ArrayList<>(); // Danh sách sau lọc

    String currentMaKhoa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spnKhoa = findViewById(R.id.spnKhoa);
        rcvGv = findViewById(R.id.rcvGv);
        fabAdd = findViewById(R.id.fabAdd);

        rcvGv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GiaoVienAdapter(this, filteredList, new GiaoVienAdapter.OnItemClickListener() {
            @Override
            public void onLongClick(GiaoVien gv) {
                showDeleteDialog(gv);
            }

            @Override
            public void onClick(GiaoVien gv) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra("GV_DATA", gv);
                startActivity(intent);
            }
        });

        rcvGv.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FormActivity.class));
        });

        loadKhoaForSpinner();
        loadAllGiaoVien(); // Load toàn bộ GV ngay từ đầu
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllGiaoVien();
    }

    // ================= LOAD TOÀN BỘ GIÁO VIÊN =================
    private void loadAllGiaoVien() {
        apiService.getListGiaoVien(null).enqueue(new Callback<List<GiaoVien>>() {
            @Override
            public void onResponse(Call<List<GiaoVien>> call, Response<List<GiaoVien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allList.clear();
                    allList.addAll(response.body());
                    filterByKhoa(currentMaKhoa);
                }
            }

            @Override
            public void onFailure(Call<List<GiaoVien>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi load Giáo Viên", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================= ĐOẠN CODE LỌC KHOA (CỦA BẠN) =================
    private void filterByKhoa(String maKhoaCanLoc) {
        filteredList.clear();

        // --- BẮT ĐẦU ĐOẠN CODE LỌC THỦ CÔNG ---
        if (maKhoaCanLoc == null || maKhoaCanLoc.isEmpty()) {
            filteredList.addAll(allList);
        } else {
            for (GiaoVien gv : allList) {
                if (gv.getMaKhoa() != null &&
                        gv.getMaKhoa().equalsIgnoreCase(maKhoaCanLoc)) {
                    filteredList.add(gv);
                }
            }
        }
        // --- KẾT THÚC ĐOẠN CODE LỌC ---

        adapter.notifyDataSetChanged();
    }

    // ================= XÓA GIÁO VIÊN =================
    private void showDeleteDialog(GiaoVien gv) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa giáo viên " + gv.getHoTen() + "?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    apiService.deleteGiaoVien(gv.getMaGV()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                                loadAllGiaoVien();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Lỗi xóa", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ================= LOAD SPINNER KHOA =================
    private void loadKhoaForSpinner() {
        apiService.getListKhoa().enqueue(new Callback<List<Khoa>>() {
            @Override
            public void onResponse(Call<List<Khoa>> call, Response<List<Khoa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Khoa> listKhoa = response.body();

                    ArrayAdapter<Khoa> spnAdapter =
                            new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, listKhoa);

                    spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnKhoa.setAdapter(spnAdapter);

                    spnKhoa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            currentMaKhoa = listKhoa.get(position).getMaKhoa();
                            filterByKhoa(currentMaKhoa);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            currentMaKhoa = "";
                            filterByKhoa("");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Khoa>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi load Khoa", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
