package br.com.avana.elivreapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.avana.elivreapp.adapter.EvaluationAdapter;
import br.com.avana.elivreapp.dao.PostDAO;
import br.com.avana.elivreapp.model.Avaliacao;
import br.com.avana.elivreapp.model.PostModel;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class FormActivity extends AppCompatActivity {

    private LatLng position;
    private PostModel post;
    private PostDAO postDAO;
    private EditText editText_title, editText_description;
    private Bundle extras;
    private Avaliacao avaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form);

        Toolbar toolbar = (Toolbar) findViewById(R.id.form_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.form_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirFirebase();
            }
        });

        post = new PostModel();

        final ImageView imgFeddback = findViewById(R.id.form_feedback);
        final TextView desc = findViewById(R.id.form_desc);

        extras = getIntent().getExtras();
        if (extras != null){
            position = (LatLng) extras.get("position");
            avaliacao = (Avaliacao) extras.get("avaliacao");

            if (position != null){
                post.setLat(position.latitude);
                post.setLng(position.longitude);
            }

            if (avaliacao != null){

                post.setAvaliacao(avaliacao);

                if (avaliacao.getAvaliacao() == Avaliacao.ANGRY_FACE){
                    desc.setText(avaliacao.getDescricao());
                    imgFeddback.setImageResource(R.drawable.ic_angry_face_color);
                }

                if (avaliacao.getAvaliacao() == Avaliacao.NEUTRAL_FACE){
                    desc.setText(avaliacao.getDescricao());
                    imgFeddback.setImageResource(R.drawable.ic_poker_face_color);
                }

                if (avaliacao.getAvaliacao() == Avaliacao.HAPPY_FACE){
                    desc.setText(avaliacao.getDescricao());
                    imgFeddback.setImageResource(R.drawable.ic_laughing_face_color);
                }

                if (avaliacao.getAvaliacao() == Avaliacao.FREE){
                    desc.setText(avaliacao.getDescricao());
                    imgFeddback.setImageResource(R.drawable.ic_free_color);
                }
            }
        }

        openTapTargetDesciption();

        editText_description = findViewById(R.id.form_description);
    }

    private void openTapTargetDesciption(){

        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.form_description)
                .setPrimaryText("Escreva algo")
                .setSecondaryText("Você pode escrever algum comentário e ele será exibido na sua ocorrência")
                .setBackgroundColour(getResources().getColor(R.color.tap_background_5))
                .setAutoDismiss(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){
                            openTapTargetSave();
                        }
                        if (state == MaterialTapTargetPrompt.STATE_DISMISSED){
                            openTapTargetSave();
                        }
                    }
                }).show();
    }

    private void openTapTargetSave(){

        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.form_save)
                .setPrimaryText("Salve sua ocorrência")
                .setSecondaryText("Clique aqui para publicar a ocorrência, assim outras pessoas poderão vê-la")
                .setBackgroundColour(getResources().getColor(R.color.tap_background_4))
                .setAutoDismiss(true)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    private void inserirFirebase(){

        // data formatada em uma string
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat(getString(R.string.form_datetime_format));
        String datetime = dateformat.format(c.getTime());

        // dados e inserir
        post.setSnippet(editText_description.getText().toString());
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
