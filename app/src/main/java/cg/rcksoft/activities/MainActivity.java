package cg.rcksoft.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.List;

import cg.rcksoft.app.R;
import cg.rcksoft.data.AppConfig;
import cg.rcksoft.data.Note;
import cg.rcksoft.data.NoteDao;
import cg.rcksoft.utils.ClipRevealFrame;
import cg.rcksoft.views.adapters.NotesAdapter;
import cg.rcksoft.views.listeners.NoteItemListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NoteItemListener, SearchView.OnQueryTextListener {

    View rootLayout;
    ClipRevealFrame menuLayout;
    View centerItem;
    private Toolbar toolbar;
    private Toolbar toolbar2;
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private FloatingActionButton fab;
    private ImageView delete;
    private ViewGroup note_ly;
    private NoteDao noteDao;
    private List<Note> notes;
    private boolean isMultipleDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            //set the transition
            Transition ts = new Explode();
            ts.setDuration(5000);
            getWindow().setEnterTransition(ts);
            getWindow().setExitTransition(ts);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new AppConfig(getApplicationContext());

        noteDao = (NoteDao) AppConfig.getReadableDatabase(Note.class);

        setUpView();
        setUpRecyclerView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if(resultCode == RESULT_OK){
            Intent i = getIntent();
            info = i.getStringExtra("success");

            if(!info.isEmpty()){
            Snackbar.make(toolbar, "Note: "+info+" ajouté", Snackbar.LENGTH_LONG).show();
            }
        }*/
    }

    private void setUpView(){

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //toolbar2 = (Toolbar)findViewById(R.id.toolbar2);
        //note_ly = (LinearLayout)findViewById(R.id.note_ly);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        //delete = (ImageView)findViewById(R.id.delete);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        //note_ly.setOnClickListener(this);
        //delete.setOnClickListener(this);
        fab.setOnClickListener(this);


        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //getSupportActionBar().setTitle("Note");
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.mipmap.box);

        }


    }

    private void setUpRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        notes = noteDao.loadAll();
        adapter = new NotesAdapter(getApplicationContext(), notes);
        adapter.setListener(this);

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.delete:{
                break;
            }*/
            /*case R.id.note_ly:{
                finish();
                break;
            }*/
            case R.id.fab:{
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onNoteItemClick(int p) {
        Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
        intent.putExtra("title", "Modifier la note");
        intent.putExtra("note", notes.get(p));
        startActivity(intent);
        Log.i("__NOTE", "" + notes.get(p).getTitle());
    }

    @Override
    public void onNoteItemLongClick(int p) {
        Log.i("__EVENT", "" + p);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //recyclerView.setAdapter(new CantiqueAdapter(getCantiques(s), getApplicationContext()));
        return true;
    }
}
