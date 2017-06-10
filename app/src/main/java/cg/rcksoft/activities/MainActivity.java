package cg.rcksoft.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity implements NoteItemListener {

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.favor: {
                break;
            }
            case R.id.action_settings: {
                break;
            }
            case R.id.about: {
                break;
            }
        }
        return true;
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

    private void setUpView() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ly_1.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
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
            title.setVisibility(View.GONE);
            anc.setImageResource(R.mipmap.close);
            isMultipleDelete = true;
        } else if (isMultipleDelete) {
            delete.setVisibility(View.GONE);
            ly_1.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            anc.setImageResource(R.mipmap.box);
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
        for (Integer p : viewMap.keySet()) {
            tabs.add(p);
        }
        Object[] _tabs = tabs.toArray();
        Arrays.sort(_tabs);
        for (int i = _tabs.length - 1; i >= 0; i--) {
            noteDao.delete(notes.get(Integer.parseInt(_tabs[i].toString())));
            adapter.deleteItems(Integer.parseInt(_tabs[i].toString()));
            viewMap.get(_tabs[i]).setBackgroundColor(color);
        }
        viewMap.clear();
    }
}
