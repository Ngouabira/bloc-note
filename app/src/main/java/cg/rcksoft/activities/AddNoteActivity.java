package cg.rcksoft.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.joda.time.LocalDateTime;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cg.rcksoft.app.R;
import cg.rcksoft.data.AppConfig;
import cg.rcksoft.data.Note;
import cg.rcksoft.data.NoteDao;
import cg.rcksoft.utils.DateUtils;

public class AddNoteActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public final static String TAG = AddNoteActivity.class.getSimpleName();
    public static final String TAG_DATEPICKER = "datepicker";
    public static final String TAG_TIMEPICKER = "timepicker";
    @BindView(R.id.toolbar2)
    Toolbar toolbar;
    @BindView(R.id.save)
    ImageView save;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.note_ly_2)
    ViewGroup note_ly;
    @BindView(R.id.ly_alert)
    ViewGroup mAlarmLayout;
    @BindView(R.id.sw)
    Switch mAlarmSw;
    @BindView(R.id.edit_note_title)
    EditText edtTitle;
    @BindView(R.id.edit_note)
    EditText edtNote;
    @BindView(R.id.edit_date)
    EditText edtDate;
    @BindView(R.id.edit_hour)
    EditText edtHour;
    @BindView(R.id.fl_btn_date)
    FloatingActionButton mFabDate;
    @BindView(R.id.fl_btn_hour)
    FloatingActionButton mFabHour;
    private int mYear, mMonth, mDay, mHour, mMin, mSec;
    private LocalDateTime mLocalDateTime;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private NoteDao noteDao;
    private Note note;

    private boolean isNote = false;
    private boolean isSwChecked = false;
    private long id;
    private String flag;


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
        ButterKnife.bind(this);

        noteDao = (NoteDao) AppConfig.getReadableDatabase(Note.class);

        setUpView();

        Bundle bd = getIntent().getExtras();

        try {
            note = bd.getParcelable("note");
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
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @OnClick(R.id.note_ly_2)
    public void homeClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.save)
    public void saveClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (!isNote) {
            if (!(getNote().getDescription().trim().equals("") && getNote().getTitle().trim().equals(""))) {
                addNote();
                //intent.putExtra("info", "Note ajoutee");
                startActivity(intent);
            } else {
                //editNote();
                //intent.putExtra("info", "Renseigner une note");
                finish();
                startActivity(intent);
            }
        } else {
            if (isSwChecked) {
                if (getNote().getDateAlarm().equals("") && getNote().getHeurAlarm().equals("")) {
                    Snackbar.make(toolbar, "Alert actif, veillez le configurée !", Snackbar.LENGTH_LONG).show();
                    //Set EditText Error
                } else {
                    editNote();
                    //intent.putExtra("info", "Renseigner une note");
                    startActivity(intent);
                }
            } else {
                editNote();
                //intent.putExtra("info", "Renseigner une note");
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.delete)
    public void deleteClick(View v) {
        showDialog();
    }

    @OnClick(R.id.fl_btn_date)
    public void fabDateClick(View v) {
        mDatePickerDialog.show(getSupportFragmentManager(), TAG_DATEPICKER);
    }

    @OnClick(R.id.fl_btn_hour)
    public void fabHourClick(View v) {
        mTimePickerDialog.show(getSupportFragmentManager(), TAG_TIMEPICKER);
    }

    @OnClick(R.id.edit_date)
    public void fieldDateClick(View v) {
        mDatePickerDialog.show(getSupportFragmentManager(), TAG_DATEPICKER);
    }

    @OnClick(R.id.edit_hour)
    public void fieldHourClick(View v) {
        mTimePickerDialog.show(getSupportFragmentManager(), TAG_TIMEPICKER);
    }

    @OnCheckedChanged(R.id.sw)
    public void switchChanged(CompoundButton buttonView, boolean isChecked) {
        swHandler(isChecked);
    }

    private void swHandler(boolean isChecked) {
        //Animation anim;
        if (isChecked) {
            mAlarmLayout.setVisibility(View.VISIBLE);
            mAlarmSw.setChecked(true);
            /*anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            mAlarmLayout.startAnimation(anim);*/
            isSwChecked = isChecked;
        } else {
            mAlarmLayout.setVisibility(View.GONE);
            mAlarmSw.setChecked(false);
            /*anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            mAlarmLayout.startAnimation(anim);*/
            isSwChecked = isChecked;
        }
    }

    private void showDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.dialog_title)
                .iconRes(R.mipmap.box)
                .titleColorRes(android.R.color.black)
                .content(R.string.message)
                .positiveText(R.string.oui)
                .negativeText(R.string.non)
                .backgroundColorRes(R.color.primary)
                .contentColorRes(android.R.color.black)
                /*.positiveColor(getResources().getColor(android.R.color.black))
                .negativeColor(getResources().getColor(android.R.color.black))*/
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        noteDao.delete(note);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).build();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    private void setUpView(){
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
        if (mLocalDateTime == null) {
            mLocalDateTime = LocalDateTime.now();
            mYear = mLocalDateTime.getYear();
            mMonth = mLocalDateTime.getMonthOfYear();
            mDay = mLocalDateTime.getDayOfMonth();
            mHour = mLocalDateTime.getHourOfDay();
            mMin = mLocalDateTime.getMinuteOfHour();
        }

        mDatePickerDialog = DatePickerDialog.newInstance(
                this,
                mYear,
                mMonth,
                mDay
        );
        mDatePickerDialog.setYearRange(mLocalDateTime.getYear(), mLocalDateTime.getYear() + 5);

        mTimePickerDialog = TimePickerDialog.newInstance(
                this,
                mHour,
                mMin,
                true, true);
        mTimePickerDialog.setOnTimeSetListener(this);
        mAlarmSw.setChecked(false);

    }

    private void setUpNote(Note note) {
        id = note.getId();
        flag = note.getFlagFavorite();
        edtTitle.setText(note.getTitle());
        edtNote.setText(note.getDescription());
        delete.setVisibility(View.VISIBLE);
        if (note.getHeurAlarm().equals("") && note.getDateAlarm().equals("")) {
            swHandler(false);
        } else {
            swHandler(true);
            edtDate.setText(note.getDateAlarm());
            edtHour.setText(note.getHeurAlarm());
        }
    }

    private Note getNote(){
        Note note = new Note();
        if (isNote) {
            note.setId(id);
            note.setFlagFavorite(flag);
        } else {
            note.setFlagFavorite("N");
        }

        if (isSwChecked) {
            note.setHeurAlarm(edtHour.getText().toString().trim());
            note.setDateAlarm(edtDate.getText().toString().trim());
        } else {
            note.setHeurAlarm("");
            note.setDateAlarm("");
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
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        mYear = year;
        mMonth = month;
        mDay = day;

        edtDate.setText(DateUtils.getNumberString(mDay) + "/" + DateUtils.getNumberString(mMonth)
                + "/" + mYear);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int min) {
        mHour = hour;
        mMin = min;

        edtHour.setText(DateUtils.getNumberString(hour) + ":" + DateUtils.getNumberString(min));
    }
}
