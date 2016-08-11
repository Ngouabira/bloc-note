package cg.rcksoft.app.tools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;

import cg.rcksoft.app.R;
import cg.rcksoft.app.tools.font.RobotoTextView;
import cg.rcksoft.data.Note;

/**
 * Created by RICKEN on 21/06/2016.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context myContext;
    List<Note> data;
    int[] color = {R.drawable.vert, R.drawable.jaune,
            R.drawable.rouge};
    int value;

    public NotesAdapter(Context myContext, List<Note> data) {
        this.myContext = myContext;
        this.data = data;
        value = -1;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder h, int p) {

        value++;

        h.title.setText(data.get(p).getTitle());
        h.info.setText(data.get(p).getDateEditNote().toString());

        h.img.setImageResource(color[(value < 3 ? value : 0)]);

        if( value == 2){
            value = -1;
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     *
     */
    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView img;
        RobotoTextView title;
        RobotoTextView info;

        public NotesViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.card_img);
            title = (RobotoTextView) itemView.findViewById(R.id.card_title);
            info = (RobotoTextView) itemView.findViewById(R.id.card_sous_title);

        }

        @Override
        public void onClick(View v) {
            action(v);
        }


        private void action(View v){
            int id = v.getId();

        }
    }
}
