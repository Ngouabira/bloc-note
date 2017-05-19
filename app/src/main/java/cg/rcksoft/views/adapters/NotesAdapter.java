package cg.rcksoft.views.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cg.rcksoft.app.R;
import cg.rcksoft.data.AppConfig;
import cg.rcksoft.data.Note;
import cg.rcksoft.utils.font.RobotoTextView;

/**
 * Created by RICKEN on 21/06/2016.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context myContext;
    List<Note> data;
    int[] color = {R.drawable.bar_bleue, R.drawable.bar_yelow,
            R.drawable.bar_orange, R.drawable.bar_red, R.drawable.bar_green};
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
        h.info.setText(AppConfig.dateFormat(data.get(p).getDateEditNote()));
        h.content.setText((data.get(p).getDescription().trim().isEmpty() ? "<vide>" : data.get(p).getDescription()));

        //Change card view color
        if (p % 2 == 0) {
            h.rootView.setCardBackgroundColor(myContext.getResources().getColor(R.color.material_grey_300));
        } else {
            h.rootView.setCardBackgroundColor(myContext.getResources().getColor(R.color.material_grey_200));
        }

        if (value >= color.length) {
            value = 0;
        }

        //Attach random bar to card
        h.img.setImageResource(color[value]);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     *
     */
    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView rootView;
        ImageView img;
        RobotoTextView title;
        RobotoTextView info;
        RobotoTextView content;

        public NotesViewHolder(View itemView) {
            super(itemView);
            rootView = (CardView) itemView;
            img = (ImageView) itemView.findViewById(R.id.card_img);
            title = (RobotoTextView) itemView.findViewById(R.id.card_title);
            info = (RobotoTextView) itemView.findViewById(R.id.card_sous_title);
            content = (RobotoTextView) itemView.findViewById(R.id.card_content);

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
