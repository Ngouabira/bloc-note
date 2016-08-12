package cg.rcksoft.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cg.rcksoft.app.tools.AppDBConfig;
import cg.rcksoft.data.Note;
import cg.rcksoft.data.NoteDao;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private ImageView save;
    private ViewGroup note_ly;

    private NoteDao noteDao;

    //handler bd
    private AppDBConfig ah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ah = new AppDBConfig(getApplicationContext());
        noteDao = (NoteDao) ah.getReadableDatabase(Note.class);

        setUpView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpView(){

        toolbar = (Toolbar)findViewById(R.id.toolbar2);
        note_ly = (LinearLayout)findViewById(R.id.note_ly_2);
        save = (ImageView)findViewById(R.id.save);

        note_ly.setOnClickListener(this);
        save.setOnClickListener(this);

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.note_ly_2:{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
