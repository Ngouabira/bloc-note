package cg.rcksoft.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Date;

import cg.rcksoft.app.R;
import cg.rcksoft.data.AppConfig;
import cg.rcksoft.data.Note;
import cg.rcksoft.data.NoteDao;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public final static String TAG = AddNoteActivity.class.getSimpleName();

    private Toolbar toolbar;
    private ImageView save;
    private ViewGroup note_ly;
    private EditText edtTitle, edtNote;

    private NoteDao noteDao;

    private boolean isNote = false;
    private long id;

    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private int mYear, mMonth, mDay, mHour, mMin, mSec = 0;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";


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
        setContentView(R.layout.activity_add_note);

        noteDao = (NoteDao) AppConfig.getReadableDatabase(Note.class);

        setUpView();

        Bundle bd = getIntent().getExtras();

        try {
            Note note = bd.getParcelable("note");
            if (note != null) {
                isNote = true;
                setUpNote(note);
            } else {
                isNote = false;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "Bundle getParcelbale null");
        }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        /*edtNote.setText("");
        edtTitle.setText("");*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpView(){

        toolbar = (Toolbar)findViewById(R.id.toolbar2);
        note_ly = (LinearLayout)findViewById(R.id.note_ly_2);
        save = (ImageView)findViewById(R.id.save);
        edtTitle = (EditText)findViewById(R.id.edit_note_title);
        edtNote = (EditText)findViewById(R.id.edit_note);

        note_ly.setOnClickListener(this);
        save.setOnClickListener(this);

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

    }

    private void setUpNote(Note note) {
        id = note.getId();
        edtTitle.setText(note.getTitle());
        edtNote.setText(note.getDescription());
    }

    private Note getNote(){
        Note note = new Note();
        if (isNote) {
            note.setId(id);
        }
        note.setTitle(edtTitle.getText().toString());
        note.setDescription(edtNote.getText().toString());
        note.setDateEditNote(new Date());
        return note;
    }

    private void addNote(){
        noteDao.insertInTx(getNote());
        setResult(RESULT_OK);
    }

    private void editNote() {
        noteDao.updateInTx(getNote());
        setResult(RESULT_OK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.note_ly_2:{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.save:{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if (!isNote) {
                    if (!(getNote().getDescription().trim().equals("") && getNote().getTitle().trim().equals(""))) {
                        addNote();
                        //intent.putExtra("info", "Note ajoutee");
                        startActivity(intent);
                        finish();
                    } else {
                        editNote();
                        //intent.putExtra("info", "Renseigner une note");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    editNote();
                    //intent.putExtra("info", "Renseigner une note");
                    startActivity(intent);
                    finish();
                }
                break;

            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {

    }
}
