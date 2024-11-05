package cl.elgueta.appevaluacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBUtil extends SQLiteOpenHelper {
    //Nombre de la base de datos
    private static final String DATABASE_NAME = "notas.db";
    private static final int DATABASE_VERSION = 1;

    //Nombre de la tabla (colección)
    private static final String TABLE_NAME = "notas";

    //columnas
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_CONTENIDO = "contenido";

    //constructo
    public DBUtil(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //crear la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITULO + " TEXT, " +
                COLUMN_CONTENIDO + " TEXT)";
        db.execSQL(createTable);
        Log.d("DBUtil", "Tabla 'notas' creada correctamente");
    }

    //Actualizar la base de datos si cambia la estructura
    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //insetar nueva nota
    public void insertarNota(String titulo, String contenido) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITULO, titulo);
        contentValues.put(COLUMN_CONTENIDO, contenido);

        db.insert(TABLE_NAME, null, contentValues);
    }

    //obtener todas las notas
    public List<String> obtenerNotas() {
        List<String> listaNotas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO));
                String contenido = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENIDO));
                listaNotas.add("Título: " + titulo + "\nContenido: " + contenido);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaNotas;
    }

    // Guardar o actualizar una nota específica (usando ID fijo para un único documento)
    public void guardarNota(String nota) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Comprobar si ya hay una nota guardada
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CONTENIDO, nota);  // Usamos COLUMN_CONTENIDO para almacenar el contenido de la nota

        if (cursor.getCount() > 0) {  // Si ya hay una nota, actualizamos
            db.update(TABLE_NAME, contentValues, COLUMN_ID + "= 1", null);
        } else {  // Si no hay nota, insertamos una nueva
            contentValues.put(COLUMN_ID, 1);  // Usamos ID 1 como clave fija
            db.insert(TABLE_NAME, null, contentValues);
        }

        cursor.close();
    }

    // Eliminar una nota por su ID
    public void eliminarNota(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Recuperar una nota específica
    public String recuperarNota() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CONTENIDO + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "= 1", null);

        String nota = "";
        if (cursor.moveToFirst()) {  // Verifica si hay un resultado
            nota = cursor.getString(0);  // Recupera el contenido de la columna de la nota
        }

        cursor.close();  // Cierra el cursor para liberar recursos
        return nota;  // Retorna la nota recuperada
    }
}
