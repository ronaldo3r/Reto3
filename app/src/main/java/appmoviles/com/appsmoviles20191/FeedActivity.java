package appmoviles.com.appsmoviles20191;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import appmoviles.com.appsmoviles20191.db.DBHandler;
import appmoviles.com.appsmoviles20191.model.Amigo;
import appmoviles.com.appsmoviles20191.model.Publicacion;
import appmoviles.com.appsmoviles20191.model.Usuario;

public class FeedActivity extends AppCompatActivity {

    private EditText et_feed_estado;
    private TextView txt_feed;
    private Button btn_feed_post;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;
    DBHandler db;
    private Usuario me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        db = DBHandler.getInstance(this);

        et_feed_estado = findViewById(R.id.et_feed_estado);
        txt_feed = findViewById(R.id.txt_feed);
        btn_feed_post = findViewById(R.id.btn_feed_post);

        txt_feed.setMovementMethod(new ScrollingMovementMethod());


        //Primero nos debemos identificar
        rtdb.getReference().child("user").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                me = dataSnapshot.getValue(Usuario.class);

                //Luego inicializarmos la escritura y la lectura
                initSending();
                initReading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void initReading() {
        //LECTURA
        ArrayList<Amigo> amigos = db.getAllAmigosOfUser(auth.getCurrentUser().getUid());
        //Me incluyo a mi
        Amigo alfa = new Amigo("0", me.getNombre(), "0", me.getTelefono(), me.getCorreo(), "0");
        amigos.add(alfa);
        for(int i=0 ; i<amigos.size() ; i++){
            rtdb.getReference().child("comentarios").child(amigos.get(i).getTelefono()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Publicacion p = dataSnapshot.getValue(Publicacion.class);
                    txt_feed.append(p.getNombre()+"\n"+p.getMensaje()+"\n\n");
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initSending() {
        btn_feed_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rtdb.getReference().child("user").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuario me = dataSnapshot.getValue(Usuario.class);

                        SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar cal = Calendar.getInstance();

                        Publicacion p = new Publicacion(me.getNombre(), fecha.format(cal.getTime()), et_feed_estado.getText().toString());
                        rtdb.getReference().child("comentarios").child(me.getTelefono()).push().setValue(p);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }
}
