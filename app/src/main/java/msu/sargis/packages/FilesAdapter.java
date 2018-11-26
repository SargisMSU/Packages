package msu.sargis.packages;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    @Nullable
    private OnFileClickListener onFileClickListener;

    private List<File> files = new ArrayList<>();
    private static final int TYPE_DIRECTORY = 0;
    private static final int TYPE_FILE = 1;

    public void setOnFileClickListener(@Nullable OnFileClickListener onFileClickListener) {
        this.onFileClickListener = onFileClickListener;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        if (viewType == TYPE_DIRECTORY)
            view = layoutInflater.inflate(R.layout.view_item_directory, viewGroup, false);
        else
            view = layoutInflater.inflate(R.layout.view_item_file, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        File file = files.get(i);
        viewHolder.nameTv.setText(file.getName());
        viewHolder.itemView.setTag(file);
    }

    @Override
    public int getItemViewType(int position) {
        File file = files.get(position);
        return file.isDirectory() ? TYPE_DIRECTORY : TYPE_FILE;
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView nameTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File file = (File) v.getTag();
                    if (onFileClickListener != null) {
                        onFileClickListener.onFileClick(file);
                    }
                }
            });
        }
    }

    public interface OnFileClickListener{
        void onFileClick(File file);
    }
}
