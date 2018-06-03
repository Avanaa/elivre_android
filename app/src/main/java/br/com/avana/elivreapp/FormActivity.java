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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.avana.elivreapp.dao.PostDAO;
import br.com.avana.elivreapp.model.Avaliacao;
import br.com.avana.elivreapp.model.PostModel;

public class FormActivity extends AppCompatActivity {

    private LatLng position;
    private PostModel post;
    private PostDAO postDAO;
    private EditText editText_title, editText_description;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.form_toolbar);
        setSupportActionBar(toolbar);

        post = new PostModel();

        extras = getIntent().getExtras();
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
                post.setAvaliacao(Avaliacao.ANGRY_FACE);
            }
        });

        ImageButton btnCu = findViewById(R.id.form_btn_poker_face);
        btnCu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFeddback.setImageResource(R.drawable.ic_poker_face_color);
                desc.setText(R.string.form_evaluation_neutral);
                post.setAvaliacao(Avaliacao.NEUTRAL_FACE);
            }
        });

        ImageButton btnFeliz = findViewById(R.id.form_btn_happy_face);
        btnFeliz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFeddback.setImageResource(R.drawable.ic_laughing_face_color);
                desc.setText(R.string.form_evaluation_happy);
                post.setAvaliacao(Avaliacao.HAPPY_FACE);
            }
        });

        ImageButton btnLivre = findViewById(R.id.form_btn_free);
        btnLivre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFeddback.setImageResource(R.drawable.ic_free_color);
                desc.setText(R.string.form_evaluation_free);
                post.setAvaliacao(Avaliacao.FREE);
            }
        });

        editText_title = (EditText) findViewById(R.id.form_title);
        editText_description = (EditText) findViewById(R.id.form_description);
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
        //post.setUsuario(FirebaseAuth.getInstance().getCurrentUser().getUid());

        new PostDAO().save(post);
        inserirFirebaseSucesso();
    }

    private void inserirFirebaseSucesso(){
        AlertDialog.Builder alert = new AlertDialog.Builder(FormActivity.this);
        alert.setTitle("Enviado!");
        alert.setMessage("Ocorrência enviada com sucesso.");
        alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        alert.show();
    }
}
