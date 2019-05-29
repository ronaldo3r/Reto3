package appmoviles.com.appsmoviles20191;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import appmoviles.com.appsmoviles20191.db.DBHandler;
import appmoviles.com.appsmoviles20191.model.Amigo;

public class MainActivity extends AppCompatActivity implements AdapterAmigos.OnItemClickListener{

    private RecyclerView lista_amigos;
    private Button btn_agregar;
    DBHandler localdb;
    private AdapterAmigos adapterAmigos;
    FirebaseAuth auth;
    private Button btn_signout;
    private Button btn_feed;

    FirebaseDatabase rtdb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent ser = new Intent(this, NotificationService.class);
        startService(ser);


        rtdb = FirebaseDatabase.getInstance();

        //rtdb.getReference().child("alfa").child("beta").child("gamma").setValue("Mi primer valor");


        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 0);

        localdb = DBHandler.getInstance(this);
        auth = FirebaseAuth.getInstance();


        //Si no hay usuario loggeado
        if(auth.getCurrentUser() == null){

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();

            return;
        }

        btn_signout = findViewById(R.id.btn_signout);
        lista_amigos = findViewById(R.id.lista_amigos);
        btn_agregar = findViewById(R.id.btn_agregar);
        adapterAmigos = new AdapterAmigos();
        adapterAmigos.setListener(this);
        lista_amigos.setLayoutManager(new LinearLayoutManager(this));
        lista_amigos.setAdapter(adapterAmigos);
        lista_amigos.setHasFixedSize(true);
        btn_feed = findViewById(R.id.btn_feed);


        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AgregarAmigoActivity.class);
                startActivity(i);
            }
        });

        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterAmigos.showAllAmigos(localdb.getAllAmigosOfUser(auth.getCurrentUser().getUid()));
    }


    @Override
    public void onItemClick(Amigo amigo) {
        Intent i = new Intent( Intent.ACTION_CALL );
        i.setData( Uri.parse("tel:"+amigo.getTelefono()) );
        startActivity(i);
    }

    @Override
    public void onChat(Amigo amigo) {
        //Vamos a abrir la ventana de chat
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("tel",amigo.getTelefono());
        startActivity(i);
    }
}

