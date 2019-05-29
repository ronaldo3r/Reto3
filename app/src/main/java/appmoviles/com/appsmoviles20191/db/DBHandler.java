package appmoviles.com.appsmoviles20191.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import appmoviles.com.appsmoviles20191.model.Amigo;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler instance = null;


    public static final String DB_NAME = "AppMoviles20191";
    public static final int DB_VERSION = 2;

    //TABLA AMIGOS
    public static final String TABLE_AMIGOS = "amigos";
    public static final String AMIGOS_ID = "id";
    public static final String AMIGOS_NOMBRE = "nombre";
    public static final String AMIGOS_EDAD = "edad";
    public static final String AMIGOS_CORREO = "correo";
    public static final String AMIGOS_TELEFONO = "telefono";
    public static final String AMIGOS_USERID = "userid";
    public static final String CREATE_TABLE_AMIGOS = "CREATE TABLE " + TABLE_AMIGOS + " (" + AMIGOS_ID + " TEXT PRIMARY KEY, " + AMIGOS_NOMBRE + " TEXT, " + AMIGOS_EDAD + " TEXT, " + AMIGOS_CORREO + " TEXT, " + AMIGOS_TELEFONO + " TEXT, " + AMIGOS_USERID + " TEXT)";

    public static synchronized DBHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DBHandler(context);
        }
        return instance;
    }


    private DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_AMIGOS);
    }

    //Se hace cuando aumentamos la version de la DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AMIGOS);
        onCreate(db);
    }

    //CRUD

    //CREATE
    public void createAmigo(Amigo amigo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_AMIGOS + " (" + AMIGOS_ID + ", " + AMIGOS_NOMBRE + ", " + AMIGOS_EDAD + ", " + AMIGOS_CORREO + ", " + AMIGOS_TELEFONO + ", " + AMIGOS_USERID + ") VALUES ('" + amigo.getId() + "','" + amigo.getNombre() + "','" + amigo.getEdad() + "','" + amigo.getEmail() + "','" + amigo.getTelefono() + "', '" + amigo.getUserID() + "')");
        db.close();
    }

    //READ
    public ArrayList<Amigo> getAllAmigosOfUser(String uid) {
        ArrayList<Amigo> respuesta = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AMIGOS + " WHERE " + AMIGOS_USERID + "='" + uid + "'", null);
        Log.e(">>>","Amigos");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Amigo amigo = new Amigo();
                amigo.setId(cursor.getString(cursor.getColumnIndex(AMIGOS_ID)));
                amigo.setNombre(cursor.getString(cursor.getColumnIndex(AMIGOS_NOMBRE)));
                amigo.setEdad(cursor.getString(cursor.getColumnIndex(AMIGOS_EDAD)));
                amigo.setEmail(cursor.getString(cursor.getColumnIndex(AMIGOS_CORREO)));
                amigo.setTelefono(cursor.getString(cursor.getColumnIndex(AMIGOS_TELEFONO)));
                amigo.setUserID(cursor.getString(cursor.getColumnIndex(AMIGOS_USERID)));
                respuesta.add(amigo);
                Log.e(">>>",amigo.getNombre());

            } while (cursor.moveToNext());
        }
        db.close();

        return respuesta;

    }

    public void deleteAmigosOfUser(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_AMIGOS + " WHERE " + AMIGOS_USERID + "='" + uid + "'");
        db.close();
    }
}
