package br.com.avana.elivreapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import br.com.avana.elivreapp.R;
import br.com.avana.elivreapp.dao.PostDAO;
import br.com.avana.elivreapp.model.PostModel;

public class FormActivity extends AppCompatActivity {

    private LatLng position;
    private PostModel post;
    private PostDAO postDAO;
    private EditText editText_title, editText_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.form_toolbar);
        setSupportActionBar(toolbar);

        post = new PostModel();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            position = (LatLng) extras.get("position");

            if (position != null){
                post.setLat(position.latitude);
                post.setLng(position.longitude);
            }
        } else {
            post.setLat(-8.409);
            post.setLng(-34.8713);
        }

        final ImageView imgFeddback = findViewById(R.id.form_feedback);
        final TextView desc = findViewById(R.id.form_desc);

        final ImageButton btnPuto = findViewById(R.id.form_btn_angry_face);
        btnPuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFeddback.setImageResource(R.drawable.ic_angry_face_color);
                desc.setText(R.string.form_evaluation_angry);
                post.setAvaliacaoString("angry");
            }
        });

        ImageButton btnCu = findViewById(R.id.form_btn_poker_face);
        btnCu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFeddback.setImageResource(R.drawable.ic_poker_face_color);
                desc.setText(R.string.form_evaluation_neutral);
                post.setAvaliacaoString("poker");
            }
        });

        ImageButton btnFeliz = findViewById(R.id.form_btn_happy_face);
        btnFeliz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFeddback.setImageResource(R.drawable.ic_laughing_face_color);
                desc.setText(R.string.form_evaluation_happy);
                post.setAvaliacaoString("happy");
            }
        });

        ImageButton btnLivre = findViewById(R.id.form_btn_free);
        btnLivre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFeddback.setImageResource(R.drawable.ic_free_color);
                desc.setText(R.string.form_evaluation_free);
                post.setAvaliacaoString("free");
            }
        });

        editText_title = (EditText) findViewById(R.id.form_title);
        editText_description = (EditText) findViewById(R.id.form_description);
        postDAO = new PostDAO();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_save:
                //Salvar no banco e voltar para mapa
                inserirFirebase();
                break;
        }
        return true;
    }

    private void inserirFirebase(){
        // data formatada em uma string
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd H:mm:ss");
        String datetime = dateformat.format(c.getTime());

        // dados e inserir
        post.setTitulo(editText_title.getText().toString());
        post.setDescricao(editText_description.getText().toString());
        post.setDataString(datetime);
        postDAO.save(post);

        inserirFirebaseSucesso();
    }

    private void inserirFirebaseSucesso(){
        AlertDialog.Builder alert = new AlertDialog.Builder(FormActivity.this);
        alert.setTitle("Enviado!");
         alert.setMessage("Ocrrência enviada com sucesso.");
        alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), FormActivity.class);
//                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        alert.show();
    }
}
