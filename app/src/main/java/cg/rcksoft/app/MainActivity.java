package cg.rcksoft.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cg.rcksoft.app.tools.AnimatorUtils;
import cg.rcksoft.app.tools.AppHelper;
import cg.rcksoft.app.tools.ClipRevealFrame;
import cg.rcksoft.app.tools.adapter.NotesAdapter;
import cg.rcksoft.data.Note;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab;
    private ImageView delete;
    private ViewGroup note_ly;

    View rootLayout;
    ClipRevealFrame menuLayout;
    ArcLayout arcLayout;
    View centerItem;

    //handler bd
    private AppHelper ah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ah = new AppHelper(getApplicationContext());

        setUpView();
        setUpRecyclerView();
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

        return super.onOptionsItemSelected(item);
    }

    private void setUpView(){

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        note_ly = (LinearLayout)findViewById(R.id.note_ly);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        delete = (ImageView)findViewById(R.id.delete);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        note_ly.setOnClickListener(this);
        delete.setOnClickListener(this);
        fab.setOnClickListener(this);

        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        /*rootLayout = findViewById(R.id.root_layout);
        menuLayout = (ClipRevealFrame) findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);*/
        //centerItem = findViewById(R.id.center_item);

    }

    private void setUpRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new NotesAdapter(getApplicationContext(), ah.getNoteDao().loadAll());

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:{
                Snackbar.make(toolbar, "Delete", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.note_ly:{
                //List<Note> n = ah.getNoteDao().loadAll();
                Snackbar.make(toolbar, "Note: ", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.fab:{

               /* int x = (v.getLeft() + v.getRight()) / 2;
                int y = (v.getTop() + v.getBottom()) / 2;
                float radiusOfFab = 1f * v.getWidth() / 2f;
                float radiusFromFabToRoot = (float) Math.hypot(
                        Math.max(x, rootLayout.getWidth() - x),
                        Math.max(y, rootLayout.getHeight() - y));

                if (v.isSelected()) {
                    hideMenu(x, y, radiusFromFabToRoot, radiusOfFab);
                } else {
                    showMenu(x, y, radiusOfFab, radiusFromFabToRoot);
                }
                v.setSelected(!v.isSelected());*/

                /*Note n = new Note();
                n.setDateEditNote(new Date());
                n.setDescription("La deuxieme note ");
                n.setTitle("The 2");

                ah.getNoteDao().insertInTx(n);
                setResult(RESULT_OK);*/
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    //=============================================================

    private void showMenu(int cx, int cy, float startRadius, float endRadius) {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        Animator revealAnim = createCircularReveal(menuLayout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);

        animList.add(revealAnim);
        /*animList.add(createShowItemAnimator(centerItem));*/

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }

    private void hideMenu(int cx, int cy, float startRadius, float endRadius) {
        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        animList.add(createHideItemAnimator(centerItem));

        Animator revealAnim = createCircularReveal(menuLayout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });

        animList.add(revealAnim);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {
       /* float dx = centerItem.getX() - item.getX();
        float dy = centerItem.getY() - item.getY();*/

        item.setScaleX(0f);
        item.setScaleY(0f);
       /* item.setTranslationX(dx);
        item.setTranslationY(dy);*/

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(0f, 1f),
                AnimatorUtils.scaleY(0f, 1f)
                /*AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)*/
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        /*final float dx = centerItem.getX() - item.getX();
        final float dy = centerItem.getY() - item.getY();*/

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(1f, 0f),
                AnimatorUtils.scaleY(1f, 0f)
                /*AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)*/
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                /*item.setTranslationX(0f);
                item.setTranslationY(0f);*/
            }
        });
        anim.setDuration(50);
        return anim;
    }

    private Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius,
                                          float endRadius) {
        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setClipOutLines(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return reveal;
    }

    private void onFabClick(View v) {
        int x = (v.getLeft() + v.getRight()) / 2;
        int y = (v.getTop() + v.getBottom()) / 2;
        float radiusOfFab = 1f * v.getWidth() / 2f;
        float radiusFromFabToRoot = (float) Math.hypot(
                Math.max(x, rootLayout.getWidth() - x),
                Math.max(y, rootLayout.getHeight() - y));

        if (v.isSelected()) {
            hideMenu(x, y, radiusFromFabToRoot, radiusOfFab);
        } else {
            showMenu(x, y, radiusOfFab, radiusFromFabToRoot);
        }
        v.setSelected(!v.isSelected());
    }



}
