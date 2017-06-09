package cg.rcksoft.activities;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cg.rcksoft.app.R;
import cg.rcksoft.data.AppConfig;
import cg.rcksoft.data.Note;
import cg.rcksoft.data.NoteDao;
import cg.rcksoft.utils.font.RobotoTextView;
import cg.rcksoft.views.adapters.NotesAdapter;
import cg.rcksoft.views.listeners.NoteItemListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NoteItemListener, SearchView.OnQueryTextListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.title)
    RobotoTextView title;
    @BindView(R.id.anc)
    ImageView anc;
    @BindView(R.id.config)
    ImageView config;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.ly_1)
    ViewGroup ly_1;
    @BindView(R.id.note_ly)
    ViewGroup note_ly;
    NotesAdapter adapter;
    private NoteDao noteDao;
    private List<Note> notes;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private boolean isMultipleDelete;
    private boolean isSearch;
    private int color = Color.TRANSPARENT;

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

        ButterKnife.bind(this);

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

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.config: {
                break;
            }
            case R.id.delete: {
                deleteNotes();
                setUpMenu();
                break;
            }
            case R.id.search: {
                if (!isSearch) {
                    editSearch.setVisibility(View.VISIBLE);
                    isSearch = true;
                } else {
                    editSearch.setVisibility(View.GONE);
                    isSearch = false;
                }
                break;
            }
            case R.id.note_ly: {
                if (title.isShown()) {
                    finish();
                } else {
                    isMultipleDelete = true;
                    setUpMenu();
                    notDeleteAll();
                }
                break;
            }
            case R.id.fab: {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
                break;
            }
        }*/
    }

    @Override
    public void onNoteItemClick(int p, View v) {
        if (!isMultipleDelete) {
            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            intent.putExtra("title", "Modifier la note");
            intent.putExtra("note", notes.get(p));
            startActivity(intent);
            Log.i("__NOTE", "" + notes.get(p).getTitle());
        } else {
            //View exist
            if (viewMap.get(p) != null) {
                v.setBackgroundColor(color);
                viewMap.remove(p);
            } else {
                v.setBackgroundColor(getResources().getColor(R.color.bac2));
                viewMap.put(Integer.valueOf(p), v);
            }

            if (viewMap.size() == 0) {
                setUpMenu();
            }
        }
    }

    @Override
    public void onNoteItemLongClick(int p, View v) {
        Log.i("__EVENT", "" + p);
        if (!isMultipleDelete) {
            v.setBackgroundColor(getResources().getColor(R.color.bac2));
            viewMap.put(Integer.valueOf(p), v);
            setUpMenu();
        } else {
            if (viewMap.size() > 0) {
                //View exist
                if (viewMap.get(p) != null) {
                    v.setBackgroundColor(color);
                    viewMap.remove(p);
                } else {
                    v.setBackgroundColor(getResources().getColor(R.color.bac2));
                    viewMap.put(Integer.valueOf(p), v);
                }
                if (viewMap.size() == 0) {
                    setUpMenu();
                }
            } else {
                setUpMenu();
            }
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

    @OnClick(R.id.fab)
    public void onFabClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.note_ly)
    public void onHomClick(View v) {
        if (title.isShown()) {
            finish();
        } else {
            isMultipleDelete = true;
            setUpMenu();
            notDeleteAll();
        }
    }

    @OnClick(R.id.search)
    public void onSearchClick(View v) {
        if (!isSearch) {
            editSearch.setVisibility(View.VISIBLE);
            isSearch = true;
        } else {
            editSearch.setVisibility(View.GONE);
            isSearch = false;
        }
    }

    @OnClick(R.id.delete)
    public void onDeleteClick(View v) {
        deleteNotes();
        setUpMenu();
    }

    @OnClick(R.id.config)
    public void onConfigClick(View v) {

    }

    private void setUpView() {

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        note_ly = (LinearLayout) findViewById(R.id.note_ly);
        ly_1 = (LinearLayout) findViewById(R.id.ly_1);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        title = (RobotoTextView) findViewById(R.id.title);
        config = (ImageView) findViewById(R.id.config);
        search = (ImageView) findViewById(R.id.search);
        delete = (ImageView) findViewById(R.id.delete);
        anc = (ImageView) findViewById(R.id.anc);
        editSearch = (EditText) findViewById(R.id.edit_search);
        fab = (FloatingActionButton) findViewById(R.id.fab);*/

        /*note_ly.setOnClickListener(this);
        delete.setOnClickListener(this);
        config.setOnClickListener(this);
        search.setOnClickListener(this);
        fab.setOnClickListener(this);*/


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
            //getSupportActionBar().setTitle("Note");
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //toolbar.setNavigationIcon(R.mipmap.box);
        }

        ly_1.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        /*anc.setImageResource(R.mipmap.box);
        config.setImageResource(R.mipmap.setting);*/

    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        notes = noteDao.loadAll();
        adapter = new NotesAdapter(getApplicationContext(), notes);
        adapter.setListener(this);
        Log.i("DATA", "" + notes.size());

        recyclerView.setAdapter(adapter);

    }

    private void setUpMenu() {
        if (!isMultipleDelete) {
            ly_1.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);
            //config.setImageResource(R.mipmap.trash);
            title.setVisibility(View.GONE);
            anc.setImageResource(R.mipmap.close);
            Log.i("isMultipleDelete show", "" + isMultipleDelete);
            isMultipleDelete = true;
        } else if (isMultipleDelete) {
            //config.setImageResource(R.mipmap.setting);
            delete.setVisibility(View.GONE);
            ly_1.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            anc.setImageResource(R.mipmap.box);
            Log.i("isMultipleDelete hide", "" + isMultipleDelete);
            isMultipleDelete = false;
        }
    }

    private void notDeleteAll() {
        for (Integer i : viewMap.keySet()) {
            (viewMap.get(i)).setBackgroundColor(color);
        }
    }

    private void deleteNotes() {
        List<Integer> tabs = new ArrayList<>();
        Log.i("___Main", "size: " + notes.size());
        Log.i("___Main", "map: " + viewMap.size());
        for (Integer p : viewMap.keySet()) {
            Log.i("___Main", "position: " + p);
            Log.i("___Main", "notes: " + notes.size());
            tabs.add(p);
        }
        Object[] _tabs = tabs.toArray();
        Arrays.sort(_tabs);
        for (int i = _tabs.length - 1; i >= 0; i--) {
            Log.i("OBJECT", "TABS: " + _tabs[i]);
            noteDao.delete(notes.get(Integer.parseInt(_tabs[i].toString())));
            adapter.deleteItems(Integer.parseInt(_tabs[i].toString()));
        }
        viewMap.clear();
    }
}
