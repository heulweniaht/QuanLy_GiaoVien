package com.example.quanly_giaovien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class GiaoVienAdapter extends RecyclerView.Adapter<GiaoVienAdapter.ViewHolder> {
    List<GiaoVien> list;
    Context context;
    OnItemClickListener listener;
    public interface OnItemClickListener{
        void onLongClick(GiaoVien gv);
        void onClick(GiaoVien gv);

    }

    public GiaoVienAdapter( Context context, List<GiaoVien> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_giao_vien,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiaoVien gv = list.get(position);
        //Hiển thị text : Học hàm/hộc vị + tên
        // Xử lý an toàn: Nếu null thì thay bằng chuỗi rỗng
        String strHocVi = (gv.getHocVi() != null) ? gv.getHocVi() : "";
        String strHoTen = (gv.getHoTen() != null) ? gv.getHoTen() : "Không tên";

        // Nối chuỗi
        // Kết quả sẽ là "Thạc sỹ Nguyễn Văn A" hoặc "Nguyễn Văn A" (nếu không có học vị)
        String title = strHocVi + " " + strHoTen;

        holder.tvHoTen.setText(title.trim());
        holder.tvKhoa.setText("Khoa: " + gv.getTenKhoa());
        //Xử lý ảnh
        String fullUrl = "";
        if(gv.getAnh() != null && !gv.getAnh().isEmpty()){
            fullUrl = ApiService.Base_Url + gv.getAnh();
        }
        Glide.with(context)
                .load(fullUrl)
                .placeholder(android.R.drawable.ic_menu_gallery) //Ảnh mặc định
                .error(android.R.drawable.ic_menu_gallery) //Ảnh lỗi
                .circleCrop() //Bo tròn
                .into(holder.imgAvt);
        //Xử lý Click
        holder.itemView.setOnClickListener(v -> listener.onClick(gv));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(gv);
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAvt;
        TextView tvHoTen;
        TextView tvKhoa;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imgAvt = itemView.findViewById(R.id.imgAvt);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvKhoa = itemView.findViewById(R.id.tvKhoa);
        }
    }

}
