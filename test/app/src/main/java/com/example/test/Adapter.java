package com.example.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.databinding.DialogAddBinding;
import com.example.test.databinding.DialogPreviewBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter extends RecyclerView.Adapter<Adapter.ComicViewHolder> {
    private Context context;
    private List<Comic> list;
    private List<Comic> fulList;
    private Comic comic;
    private MainActivity mainActivity;

    public Adapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        this.fulList = new ArrayList<>();
    }

    public void setList(List<Comic> list) {
        this.list = list;
        this.fulList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_comic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comic current = list.get(holder.getAdapterPosition());
        if (current == null) {
            return;
        }
        Glide.with(context).load(current.getImage()).error(R.drawable.i8).into(holder.art);
        holder.title.setText(current.getTitle());
        holder.sua.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Comic obj = list.get(position);
                    showDialogUpdate(obj, position); // Truyền vào vị trí của item
                }
            }
        });
        holder.xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Comic obj = list.get(position);
                    showDeleteConfirmationDialog(position, obj.getId());
                }
            }
        });
        holder.tv_create_date.setText(current.getCreatedAt());
        holder.itemSelected.setOnClickListener(v -> {
            showDialogPreview(current, holder.getAdapterPosition());
        });
    }

    private void showDialogPreview(Comic comic, int adapterPosition) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        DialogPreviewBinding binding = DialogPreviewBinding.inflate(LayoutInflater.from(context));
        dialog.setView(binding.getRoot());
        AlertDialog alertDialog = dialog.create();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.color.white);

        Glide.with(context).load(comic.getImage()).error(R.drawable.i8).into(binding.imgArt);
        binding.tvTitle.setText(comic.getTitle());
        binding.tvContent.setText(comic.getContent());
        binding.tvStatus.setText(String.valueOf(comic.getStatus()));
        binding.tvCreateDate.setText(comic.getCreatedAt());
        binding.tvEnddate.setText(comic.getEnd_date());

        dialog.show();
    }

    private void showDeleteConfirmationDialog(int position, String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteComic(id, position);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void deleteComic(String id, int position) {
        Api.instance.deleteComic(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                list.remove(position);
                notifyItemRemoved(position);

                Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Delete fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogUpdate(Comic comic, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        DialogAddBinding binding = DialogAddBinding.inflate(LayoutInflater.from(context));
        dialog.setView(binding.getRoot());
        AlertDialog alertDialog = dialog.create();

        Window window = alertDialog.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(R.color.white);

        binding.edtTitle.setText(comic.getTitle());
        binding.edtContent.setText(comic.getContent());
        binding.edtStatus.setText(String.valueOf(comic.getStatus()));
        binding.edtImg.setText(comic.getImage());
        binding.edtEndDate.setText(comic.getEnd_date());

        binding.btnCancel.setOnClickListener(v -> {
            binding.edtTitle.setText("");
            binding.edtContent.setText("");
            binding.edtImg.setText("");
            binding.edtStatus.setText("");
            binding.edtEndDate.setText("");
            alertDialog.dismiss();
        });
        binding.btnSave.setOnClickListener(v -> {
            String title = binding.edtTitle.getText().toString();
            String content = binding.edtContent.getText().toString();
            String img = binding.edtImg.getText().toString();
            String enddate = binding.edtEndDate.getText().toString();
            int status = Integer.parseInt(binding.edtStatus.getText().toString());

            comic.setTitle(title);
            comic.setContent(content);
            comic.setImage(img);
            comic.setEnd_date(enddate);
            comic.setStatus(status);
            updateComic(comic.getId(), comic);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void updateComic(String id, Comic comic) {
        Api.instance.editComic(id, comic).enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(Call<Comic> call, Response<Comic> response) {
                notifyDataSetChanged();
                Toast.makeText(context, "update successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Comic> call, Throwable t) {
                Toast.makeText(context, "update fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ComicViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView tv_create_date;

        private ImageView sua, xoa;

        private ImageView art;

        private CardView itemSelected;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            sua = itemView.findViewById(R.id.sua);
            xoa = itemView.findViewById(R.id.xoa);
            title = itemView.findViewById(R.id.tv_title);
            tv_create_date = itemView.findViewById(R.id.tv_create_date);
            art = itemView.findViewById(R.id.img_art);
            itemSelected = itemView.findViewById(R.id.item_selected);
        }
    }
}
