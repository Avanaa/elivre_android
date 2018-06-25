package br.com.avana.elivreapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import br.com.avana.elivreapp.dao.PostDAO;
import br.com.avana.elivreapp.model.Avaliacao;
import br.com.avana.elivreapp.model.PostModel;
import br.com.avana.elivreapp.pref.Preferences;
import br.com.avana.elivreapp.quickstart.TapTarget;
import br.com.avana.elivreapp.util.DateConvert;

public class FormActivity extends AppCompatActivity {

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

        final ImageView imgFeddback = findViewById(R.id.form_feedback);
        final TextView desc = findViewById(R.id.form_desc);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            LatLng position = (LatLng) extras.get("position");
            Avaliacao avaliacao = (Avaliacao) extras.get("avaliacao");

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
        editText_description = findViewById(R.id.form_description);

        if (Preferences.isFirstTimeSeenFormScreen(this)){
            openTapTargets();
            Preferences.setFormScreenViewed(this);
        }
    }

    private void openTapTargets() {

        TapTarget targetSave = new TapTarget(this,
                R.id.action_save,
                R.string.tap_save_title,
                R.string.tap_save_subtitle,
                R.color.tap_background_4,
                null);

        TapTarget targetDescription = new TapTarget(this,
                R.id.form_description,
                R.string.tap_description_title,
                R.string.tap_description_subtitle,
                R.color.tap_background_5,
                targetSave);

        targetDescription.getBuilder().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save){
            inserirFirebase();
        }
        return true;
    }

    private void inserirFirebase(){

        // data formatada em uma string
        String datetime = DateConvert.getDateFormatByCalendar(this, Calendar.getInstance());

        // dados e inserir
        post.setSnippet(editText_description.getText().toString());
        post.setDataString(datetime);
        //post.setUsuario(FirebaseAuth.getInstance().getCurrentUser().getUid());

        new PostDAO(this).save(post);
        inserirFirebaseSucesso();
    }

    private void inserirFirebaseSucesso(){
        AlertDialog.Builder alert = new AlertDialog.Builder(FormActivity.this);
        alert.setTitle(R.string.form_send_title);
        alert.setMessage(R.string.form_send_subtitle);
        alert.setPositiveButton(R.string.form_ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        alert.show();
    }
}
