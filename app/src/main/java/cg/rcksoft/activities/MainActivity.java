package cg.rcksoft.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import cg.rcksoft.app.R;
import cg.rcksoft.data.AppConfig;
import cg.rcksoft.data.Note;
import cg.rcksoft.data.NoteDao;
import cg.rcksoft.utils.ClipRevealFrame;
import cg.rcksoft.utils.font.RobotoTextView;
import cg.rcksoft.views.adapters.NotesAdapter;
import cg.rcksoft.views.listeners.NoteItemListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NoteItemListener, SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private EditText editSearch;
    private FloatingActionButton fab;
    private RobotoTextView title;
    private ImageView anc, config, search;
    private ViewGroup note_ly, ly_2, ly_1;
    private NoteDao noteDao;
    private List<Note> notes;
    private boolean isMultipleDelete;
    private boolean isSearch;

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
        isMultipleDelete = false;
        isSearch = false;

        noteDao = (NoteDao) AppConfig.getReadableDatabase(Note.class);

        setUpView();
        setUpRecyclerView();
        //setUpMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
        note_ly = (LinearLayout)findViewById(R.id.note_ly);
        //ly_2 = (LinearLayout)findViewById(R.id.ly_2);
        ly_1 = (LinearLayout)findViewById(R.id.ly_1);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        title = (RobotoTextView) findViewById(R.id.title);
        config = (ImageView)findViewById(R.id.config);
        search = (ImageView)findViewById(R.id.search);
        anc = (ImageView)findViewById(R.id.anc);
        editSearch = (EditText) findViewById(R.id.edit_search);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        note_ly.setOnClickListener(this);
        //delete.setOnClickListener(this);
        config.setOnClickListener(this);
        search.setOnClickListener(this);
        fab.setOnClickListener(this);


        if(toolbar != null){
            setSupportActionBar(toolbar);
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
            //getSupportActionBar().setTitle("Note");
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //toolbar.setNavigationIcon(R.mipmap.box);
        }

        search.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        anc.setImageResource(R.mipmap.box);
        config.setImageResource(R.mipmap.setting);

    }

    private void setUpRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        notes = noteDao.loadAll();
        adapter = new NotesAdapter(getApplicationContext(), notes);
        adapter.setListener(this);

        recyclerView.setAdapter(adapter);

    }

    private void setUpMenu(){
        if (!isMultipleDelete){
            search.setVisibility(View.INVISIBLE);
            config.setImageResource(R.mipmap.trash);
            title.setVisibility(View.INVISIBLE);
            anc.setImageResource(R.mipmap.close);
            Log.i("isMultipleDelete show", ""+isMultipleDelete);
            isMultipleDelete = true;
        }else if (isMultipleDelete){
            config.setImageResource(R.mipmap.setting);
            search.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            anc.setImageResource(R.mipmap.box);
            Log.i("isMultipleDelete hide", ""+isMultipleDelete);
            isMultipleDelete = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.config:{
                break;
            }case R.id.search:{
                if (!isSearch){
                    editSearch.setVisibility(View.VISIBLE);
                    isSearch = true;
                }else {
                    editSearch.setVisibility(View.INVISIBLE);
                    isSearch = false;
                }
                break;
            }
            case R.id.note_ly:{
                if (title.isEnabled()){
                    finish();
                }else {
                    isMultipleDelete = true;
                    setUpMenu();
                }
                break;
            }
            case R.id.fab:{
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onNoteItemClick(int p) {
        if (!isMultipleDelete){
            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            intent.putExtra("title", "Modifier la note");
            intent.putExtra("note", notes.get(p));
            startActivity(intent);
            Log.i("__NOTE", "" + notes.get(p).getTitle());
        }else {
            Snackbar.make(toolbar, "Select more items", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNoteItemLongClick(int p) {
        Log.i("__EVENT", "" + p);
        if (!isMultipleDelete){
            setUpMenu();
        }else {
            setUpMenu();
        }
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
