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

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private ImageView save;
    private ViewGroup note_ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setUpView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
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
                //add info to show on main check if content is not empty
                startActivity(intent);
                break;
            }
        }
    }
}
