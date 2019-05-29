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

import org.w3c.dom.Text;

import appmoviles.com.appsmoviles20191.model.Mensaje;
import appmoviles.com.appsmoviles20191.model.Usuario;

public class ChatActivity extends AppCompatActivity {

    private String telefonoAmigo;
    private String telefonoPropio;
    private String idChat;
    private String nombre;
    FirebaseDatabase rtdb;
    FirebaseAuth auth;

    private EditText et_mensaje_chat;
    private Button btn_enviar_chat;
    private TextView txt_mensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        et_mensaje_chat = findViewById(R.id.et_mensaje_chat);
        btn_enviar_chat = findViewById(R.id.btn_enviar_chat);
        txt_mensajes = findViewById(R.id.txt_mensajes);
        txt_mensajes.setMovementMethod(new ScrollingMovementMethod());

        rtdb = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        telefonoAmigo = getIntent().getExtras().getString("tel");

        //Si no se mi propio teléfono
        rtdb.getReference().child("user").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario me = dataSnapshot.getValue(Usuario.class);
                telefonoPropio = me.getTelefono();
                nombre = me.getNombre();
                //Despuesa de saber los teléfonos de ambos, podemos cargar o crear los chats
                initChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initChat() {
        rtdb.getReference().child("chat").child(telefonoPropio).child(telefonoAmigo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    String pushID = rtdb.getReference().child("chat").child(telefonoPropio).child(telefonoAmigo).push().getKey();
                    //Crear ramas gemelas
                    rtdb.getReference().child("chat").child(telefonoPropio).child(telefonoAmigo).setValue(pushID);
                    rtdb.getReference().child("chat").child(telefonoAmigo).child(telefonoPropio).setValue(pushID);
                    idChat = pushID;
                }else {
                    idChat = dataSnapshot.getValue(String.class);
                }
                activarListenerBoton();
                cargarMensajes();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cargarMensajes() {
        rtdb.getReference().child("mensajes").child(idChat).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Carga todos los hijos de la rama y queda pendiente de los nuevos que se agregan
                txt_mensajes.append(dataSnapshot.getValue(Mensaje.class).nombre+"\n"+dataSnapshot.getValue(Mensaje.class).contenido + "\n\n");
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

    private void activarListenerBoton() {
        btn_enviar_chat.setVisibility(View.VISIBLE);
        btn_enviar_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Vamos a enviar los mensajes
                String mensaje = et_mensaje_chat.getText().toString();
                Mensaje m = new Mensaje(nombre, mensaje);
                rtdb.getReference().child("mensajes").child(idChat).push().setValue(m);
            }
        });
    }

}
