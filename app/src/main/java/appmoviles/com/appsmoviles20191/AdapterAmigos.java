package appmoviles.com.appsmoviles20191;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import appmoviles.com.appsmoviles20191.model.Amigo;

public class AdapterAmigos extends RecyclerView.Adapter<AdapterAmigos.CustomViewHolder> {

    ArrayList<Amigo> data;
    FirebaseStorage storage;

    public void showAllAmigos(ArrayList<Amigo> allAmigos) {
        for(int i = 0 ; i<allAmigos.size() ; i++){
            if(!data.contains(allAmigos.get(i))) data.add(allAmigos.get(i));
        }
        notifyDataSetChanged();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public AdapterAmigos(){
        data = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_amigos, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        ((TextView) holder.root.findViewById(R.id.renglon_amigo_nombre)).setText(data.get(position).getNombre());
        ((TextView) holder.root.findViewById(R.id.renglon_amigo_telefono)).setText(data.get(position).getTelefono());
        holder.root.findViewById(R.id.renglon_amigo_btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(data.get(position));
            }
        });
        holder.root.findViewById(R.id.renglon_amigo_btn_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChat(data.get(position));
            }
        });

        storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference().child("profiles").child(data.get(position).getTelefono());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView img = holder.root.findViewById(R.id.renglon_amigo_img);
                Glide.with(holder.root.getContext()).load(uri).into(img);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //OBSERVER
    public interface OnItemClickListener{
        void onItemClick(Amigo amigo);
        void onChat(Amigo amigo);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }




}
