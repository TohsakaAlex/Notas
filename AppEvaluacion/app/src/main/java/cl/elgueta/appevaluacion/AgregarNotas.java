package cl.elgueta.appevaluacion;

import static cl.elgueta.appevaluacion.R.id.btn_GuardarNota;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AgregarNotas extends AppCompatActivity {
    private TextView editTextTitulo;
    private EditText editTextNota;
    private Button btn_Guardar, btn_Recuperar;
    private  DBUtil dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_notas);

        //enlazar vistas
        editTextTitulo = findViewById(R.id.txt_tituloPaginaNota);
        editTextNota = findViewById(R.id.txt_Nota_contenido);
        btn_Guardar = findViewById(R.id.btn_GuardarNota);
        btn_Recuperar = findViewById(R.id.btn_RecuperarNota);

        //iniciar base de datos
        dbHelper = new DBUtil(this);

        //Guardar la nota cuando se usa el boton guardar
        btn_Guardar.setOnClickListener(v ->{
            String titulo = editTextTitulo.getText().toString();
            String nota = editTextNota.getText().toString();

            //validacion para no guardar con espacio vacio
            if (TextUtils.isEmpty(nota)){
                Toast.makeText(AgregarNotas.this, "por favor, escriba una nota", Toast.LENGTH_SHORT).show();
                return;
            }

            //guardar en sqlite
            dbHelper.guardarNota(nota);
            Toast.makeText(AgregarNotas.this, "Nota guardada correctaemte", Toast.LENGTH_SHORT).show();

            //Limpiar el campo despues de guardar
            editTextNota.setText("");
            editTextNota.setText("");
        });

        //Recuperar la nota al presionar Recuperar
        btn_Recuperar.setOnClickListener(v -> {
            String notaGuardada = dbHelper.recuperarNota();

            if (!TextUtils.isEmpty(notaGuardada)){
                editTextNota.setText(notaGuardada);
                Toast.makeText(AgregarNotas.this, "Nota recuperada", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(AgregarNotas.this, "No hay notas guardadas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}