package com.github.tntkhang.ui.call_records;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.nextlogix.tntkhang.R;
import com.github.tntkhang.models.database.entitiy.CallDetailEntity;
import com.github.tntkhang.models.database.repository.CallDetailRepository;

import static android.support.v4.content.FileProvider.getUriForFile;

public class CallRecordAdapter extends RecyclerView.Adapter<CallRecordAdapter.ViewHolder> {

    private final List<CallDetailEntity> callDetailEntities;
    private CallDetailRepository callDetailRepository;

    public CallRecordAdapter(CallDetailRepository callDetailRepository, List<CallDetailEntity> callDetailEntities) {
        this.callDetailRepository = callDetailRepository;
        this.callDetailEntities = callDetailEntities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = callDetailEntities.get(position);
        holder.mIdView.setText(holder.mItem.getPhoneNumber() +  (!holder.mItem.getContactName().isEmpty() ? " - " + holder.mItem.getContactName() : ""));
        holder.mContentView.setText(holder.mItem.getTime() + " - " + holder.mItem.getDate());

        holder.lnContainer.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = new File(holder.mItem.getRecordPath());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(getUriForFile(holder.mView.getContext(), "com/github/tntkhang",file), "audio/*");
            holder.mView.getContext().startActivity(intent);
        });

        holder.lnContainer.setOnLongClickListener(view -> {
            new AlertDialog.Builder(holder.mView.getContext())
                    .setIcon(ContextCompat.getDrawable(holder.mView.getContext(), R.drawable.ic_warning))
                    .setMessage("Bạn muốn xóa file ghi âm này ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                            File audioFile = new File(holder.mItem.getRecordPath());
                            if (audioFile.exists()) {
                                if (audioFile.delete()) {
                                    removeItem(holder.mItem);
                                    callDetailRepository.delete(holder.mItem);
                                } else {
                                    Toast.makeText(holder.mView.getContext(), "Xóa ko thành công", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(holder.mView.getContext(), "File ko tồn tại", Toast.LENGTH_SHORT).show();
                            }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    private void removeItem(CallDetailEntity callDetailEntity) {
        int position = callDetailEntities.indexOf(callDetailEntity);
        callDetailEntities.remove(callDetailEntity);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return callDetailEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        @BindView(R.id.container)
        LinearLayout lnContainer;

        @BindView(R.id.item_number)
        TextView mIdView;

        @BindView(R.id.content)
        TextView mContentView;

        CallDetailEntity mItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
