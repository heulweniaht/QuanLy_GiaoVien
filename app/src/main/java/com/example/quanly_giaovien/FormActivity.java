package com.example.quanly_giaovien;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {
    EditText edtMaGV, edtHoTen, edtHocHam, edtHocVi, edtEmail, edtSDT, edtLinkAnh;
    Spinner spnKhoa;
    Button btnLuu, btnHuy;
    TextView tvTitle;
    ApiService apiService;
    GiaoVien gvEdit; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        initView();
        apiService = ApiClient.getClient().create(ApiService.class);
        //Lấy dữ liệu Intent
        gvEdit = (GiaoVien) getIntent().getSerializableExtra("GV_DATA");
        //Load danh sách khoa cho Spinner
        loadSpinnerKhoa();
    }

    private void loadSpinnerKhoa() {
        apiService.getListKhoa().enqueue(new Callback<List<Khoa>>() {
            @Override
            public void onResponse(Call<List<Khoa>> call, Response<List<Khoa>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Khoa> listKhoa = response.body();
                    ArrayAdapter<Khoa> adapter = new ArrayAdapter<>(FormActivity.this, android.R.layout.simple_spinner_dropdown_item, listKhoa);
                    spnKhoa.setAdapter(adapter);
                    //Setup dữ liệu lên form
                    if(gvEdit != null){
                        setupEditData(listKhoa);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Khoa>> call, Throwable t) {
                Toast.makeText(FormActivity.this, "Lỗi load Khoa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEditData(List<Khoa> listKhoa) {
        tvTitle.setText("CẬP NHẬT GIÁO VIÊN");
        btnLuu.setText("Cập Nhật");

        edtMaGV.setText(gvEdit.getMaGV());
        edtMaGV.setEnabled(false); // Không cho sửa Mã
        edtHoTen.setText(gvEdit.getHoTen());
        edtHocHam.setText(gvEdit.getHocHam());
        edtHocVi.setText(gvEdit.getHocVi());
        edtEmail.setText(gvEdit.getEmail());
        edtSDT.setText(gvEdit.getSoDienThoai());
        edtLinkAnh.setText(gvEdit.getAnh());

        // Chọn đúng khoa trong spinner
        for (int i = 0; i < listKhoa.size(); i++) {
            if (listKhoa.get(i).getMaKhoa().equals(gvEdit.getMaKhoa())) {
                spnKhoa.setSelection(i);
                break;
            }
        }
    }

    private void initView() {
        edtMaGV = findViewById(R.id.edtMaGV);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtHocHam = findViewById(R.id.edtHocHam);
        edtHocVi = findViewById(R.id.edtHocVi);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtLinkAnh = findViewById(R.id.edtLinkAnh);
        spnKhoa = findViewById(R.id.spnKhoaForm);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);
        tvTitle = findViewById(R.id.tvTitle);
        
        btnHuy.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(v -> saveGiaoVien());
    }

    private void saveGiaoVien() {
        GiaoVien gv = new GiaoVien();
        gv.setMaGV(edtMaGV.getText().toString());
        gv.setHoTen(edtHoTen.getText().toString());
        gv.setHocHam(edtHocHam.getText().toString());
        gv.setHocVi(edtHocVi.getText().toString());
        gv.setEmail(edtEmail.getText().toString());
        gv.setSoDienThoai(edtSDT.getText().toString());
        gv.setAnh(edtLinkAnh.getText().toString());

        Khoa selectedKhoa = (Khoa) spnKhoa.getSelectedItem();
        if (selectedKhoa != null) {
            gv.setMaKhoa(selectedKhoa.getMaKhoa());
            gv.setTenKhoa(selectedKhoa.getTenKhoa());
        }
        if (gvEdit != null) {
            // Khi cập nhật, cần giữ nguyên userID của đối tượng cũ (nếu backend cần)
            gv.setUserID(gvEdit.getUserID());
        }
        // ===== LOG DỮ LIỆU GỬI LÊN SERVER =====
        Log.d("SAVE_GV", "MaGV: " + gv.getMaGV());
        Log.d("SAVE_GV", "HoTen: " + gv.getHoTen());
        Log.d("SAVE_GV", "HocHam: " + gv.getHocHam());
        Log.d("SAVE_GV", "HocVi: " + gv.getHocVi());
        Log.d("SAVE_GV", "Email: " + gv.getEmail());
        Log.d("SAVE_GV", "SDT: " + gv.getSoDienThoai());
        Log.d("SAVE_GV", "Anh: " + gv.getAnh());
        Log.d("SAVE_GV", "MaKhoa: " + gv.getMaKhoa());
        Log.d("SAVE_GV", "MODE: " + (gvEdit == null ? "ADD" : "UPDATE"));
        // =====================================

        if (gvEdit == null) {
            // Code Thêm mới (giữ nguyên hoặc kiểm tra Log lỗi tương tự bên dưới)
            apiService.addGiaoVien(gv).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // Hiển thị lỗi rõ ràng để debug
                        Toast.makeText(FormActivity.this, "Lỗi thêm (Code: " + response.code() + ")", Toast.LENGTH_LONG).show();
                        Log.e("API_ERROR", "Add failed: " + response.errorBody());
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(FormActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Code Cập nhật
            apiService.updateGiaoVien(gv.getMaGV(), gv).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(FormActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // --- ĐÂY LÀ CHỖ BẠN BỊ "IM LẶNG" ---
                        // Thêm Toast để biết lỗi gì
                        Toast.makeText(FormActivity.this, "Lỗi cập nhật (Code: " + response.code() + ")", Toast.LENGTH_LONG).show();
                        Log.e("API_ERROR", "Update failed. Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("API_ERROR", "Update error", t);
                    Toast.makeText(FormActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}