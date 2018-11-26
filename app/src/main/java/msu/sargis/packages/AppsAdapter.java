package msu.sargis.packages;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private List<AppInfo> apps = new ArrayList<>();
    private List<AppInfo> filteredApps = new ArrayList<>();
    private String query = "";

    public void setApps(List<AppInfo> apps) {
        this.apps = apps;
        filterApps();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_item_app, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AppInfo appInfo = filteredApps.get(i);
        viewHolder.iconIv.setImageDrawable(appInfo.getIcon());
        viewHolder.nameTv.setText(appInfo.getName());
        viewHolder.versionTv.setText(appInfo.getVersionName());
        viewHolder.itemView.setTag(appInfo);
    }


    @Override
    public int getItemCount() {
        return filteredApps.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView iconIv;
        private final TextView nameTv;
        private final TextView versionTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconIv = itemView.findViewById(R.id.icon_iv);
            nameTv = itemView.findViewById(R.id.name_tv);
            versionTv = itemView.findViewById(R.id.version_tv);
        }
    }

    private void filterApps(){
        filteredApps.clear();

        if (query.isEmpty()){
            filteredApps.addAll(apps);
        }else {
            for (AppInfo app :apps) {
                if (app.getName().toLowerCase().contains(query)){
                    filteredApps.add(app);
                }
            }
        }
    }

    public void setQuery(String string){
        this.query = string;
        filterApps();
    }
}
