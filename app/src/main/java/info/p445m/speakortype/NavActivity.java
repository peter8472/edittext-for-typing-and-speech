package info.p445m.speakortype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.*;



import java.util.List;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        android.speech.tts.TextToSpeech.OnInitListener {
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final int TALK_CODE = 1;
    private static final int MY_DATA_CHECK_CODE = 2;
    private static final String TAG = "speak";
    public static final String EXTRA_MESSAGE = "info.p445m.speakortype.MESSAGE";
    SpeechRecognizer recog;
    private TextToSpeech myTts=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        EditText t = findViewById(R.id.editText2);
        String mycontent = sharedPref.getString(getString(R.string.content), "why didn't it work??");
        t.setText(mycontent);
        recog = SpeechRecognizer.createSpeechRecognizer(this);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    protected void runSpeechRecognizer() {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }
    protected void check_speech() {
        if (myTts==null){
             Intent intent = new Intent();
            intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(intent, MY_DATA_CHECK_CODE);
        } else {
            myTts.setLanguage(java.util.Locale.US);
            String myText = "This is an example";
            myTts.speak(myText, TextToSpeech.QUEUE_FLUSH, null);
        }
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
            int start = t.getSelectionStart();
            int end = t.getSelectionEnd();
            e.replace(start,end, spokenText);
            t.setText(e);
        

        } else if (requestCode == MY_DATA_CHECK_CODE) {
            EditText t = findViewById(R.id.message);
            Editable e = t.getEditableText();
            int start = t.getSelectionStart();
            int end = t.getSelectionEnd();
            if (resultCode ==  android.speech.tts.TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
               e.replace(start,end, "speech is okay");
                t.setText(e);
                myTts = new TextToSpeech(this,this);
            } else {
                e.replace(start,end, "error in check voice data tts whatever");
                t.setText(e);
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        


        } else if (requestCode == TALK_CODE  ) {
            // resultCode ;

        }

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
        if (id == R.id.action_viewmessages) {
            Log.e("", "viewmessages called");
            Intent intent = new Intent(this,LandscapeActivity.class);
            EditText editText = findViewById(R.id.editText2);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
            Snackbar.make(editText, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            //editText.setText("fullscrren cliockrd");
        }
        if (id == R.id.action_record) {
            //Log.e("fullscreen", "fullscreen called");
            Intent intent = new Intent(this,Recorder.class);
            EditText editText = findViewById(R.id.editText2);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        onOptionsItemSelected(item);

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Log.e(TAG, "cam cxhosen");
            EditText t = findViewById(R.id.message);
            t.setText("started speech");
            check_speech();




        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onInit(int status) {

    }

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

}
