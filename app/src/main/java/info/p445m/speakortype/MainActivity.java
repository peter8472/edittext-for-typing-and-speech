package info.p445m.speakortype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.rtp.AudioStream;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    SpeechRecognizer recog;
    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        EditText t = findViewById(R.id.editText2);
        String mycontent = t.getText().toString();
        SharedPreferences.Editor  editor = sharedPref.edit();
        editor.putString(getString(R.string.content), mycontent);
        editor.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        EditText t = findViewById(R.id.editText2);
        String mycontent = sharedPref.getString(getString(R.string.content), "why didn't it work??");
        t.setText(mycontent);
        recog = SpeechRecognizer.createSpeechRecognizer(this);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_clear) {
            EditText t = findViewById(R.id.editText2);
            t.setText("");
        }
        if (id == R.id.action_lorem) {
            EditText t = findViewById(R.id.editText2);
            t.setText(R.string.lorem);
        }
        if (id == R.id.action_recognize) {
            runSpeechRecognizer();
        }


        return super.onOptionsItemSelected(item);
    }
    protected void runSpeechRecognizer() {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText??
            EditText t = findViewById(R.id.editText2);
            Editable e = t.getEditableText();
            e.append(spokenText);
            t.setText(e);
            //AudioStream v =  new AudioStream()
        }
    }




}
