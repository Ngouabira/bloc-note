package cg.rcksoft.views.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;

import cg.rcksoft.app.R;
import cg.rcksoft.data.AppConfig;
import cg.rcksoft.data.Note;
import cg.rcksoft.utils.font.RobotoTextView;
import cg.rcksoft.views.listeners.NoteItemListener;

import static cg.rcksoft.activities.AddNoteActivity.TAG;

/**
 * Created by RICKEN on 21/06/2016.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    Context myContext;
    List<Note> data;
    int[] color = {R.drawable.bar_bleue, R.drawable.bar_yelow,
            R.drawable.bar_orange, R.drawable.bar_red, R.drawable.bar_green};
    int value;
    NoteItemListener listener;

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
        final Note note = data.get(p);

        h.title.setText(note.getTitle().trim().isEmpty() ? "<Pas de titre>" : note.getTitle());
        h.info.setText(AppConfig.dateFormat(note.getDateEditNote()));
        h.content.setText((note.getDescription().trim().isEmpty() ? "<Accun contenu>" :
                (note.getDescription())));
        //Set favorite
        if (note.getFlagFavorite().equals("F")) {
            h.favorite.setImageResource(R.drawable.ic_start_plain);
        } else {
            h.favorite.setImageResource(R.drawable.ic_start);
        }

        //Set Alarm
        if (note.getDateAlarm().equals("") && note.getHeurAlarm().equals("")) {
            h.alarm.setImageResource(R.drawable.ic_no_alarm);
        } else {
            h.alarm.setImageResource(R.drawable.ic_notif_actif);
        }

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

    @Deprecated
    private String getContenLabel(String desc) {
        if (desc.length() > 21) {
            return desc.substring(0, 20) + "...";
        }
        return desc;
    }

    public void setListener(NoteItemListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteItems(int p) {
        this.data.remove(p);
        this.notifyItemRemoved(p);
        this.notifyDataSetChanged();
    }

    public void filterData(List<Note> notes) {
        this.data.clear();
        this.data.addAll(notes);
        this.notifyDataSetChanged();
    }

    public void rtData(int p) {

    }

    public void initData() {

    }

    /**
     *
     */
    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        CardView rootView;
        ImageView img;
        ImageView favorite;
        ImageView alarm;
        RobotoTextView title;
        RobotoTextView info;
        RobotoTextView content;
        MaterialRippleLayout view;

        public NotesViewHolder(View itemView) {
            super(itemView);
            rootView = (CardView) itemView;
            img = (ImageView) itemView.findViewById(R.id.card_img);
            favorite = (ImageView) itemView.findViewById(R.id.img_favorite);
            alarm = (ImageView) itemView.findViewById(R.id.img_alarm);
            title = (RobotoTextView) itemView.findViewById(R.id.card_title);
            info = (RobotoTextView) itemView.findViewById(R.id.card_sous_title);
            content = (RobotoTextView) itemView.findViewById(R.id.card_content);
            view = (MaterialRippleLayout) itemView.findViewById(R.id.material_ly);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick view: ");
                    if (listener != null) {
                        listener.onNoteItemClick(getAdapterPosition(), v);

                    }
                }
            });
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick favorite: ");
                    if (listener != null) {
                        listener.onNoteFavoriteClick(getAdapterPosition(), favorite);
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onNoteItemLongClick(getAdapterPosition(), v);
                    }
                    return true;
                }
            });
            favorite.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onNoteFavoriteLongClick(getAdapterPosition(), favorite);
                    }
                    return true;
                }
            });

        }

        @Override
        public void onClick(View v) {
           /* int id = v.getId();
            if (listener != null) {
                if (v == view) {
                    listener.onNoteItemClick(getAdapterPosition(), v);
                }
                if (v == favorite) {
                    listener.onNoteFavoriteClick(getAdapterPosition(), v);
                }
            }*/
        }

        @Override
        public boolean onLongClick(View v) {
            /*int id = v.getId();
            if (listener != null) {
                if (id == R.id.material_ly) {
                    listener.onNoteItemLongClick(getAdapterPosition(), v);
                }
                if (id == R.id.img_favorite) {
                    listener.onNoteFavoriteLongClick(getAdapterPosition(), v);
                }
            }*/
            return true;
        }

    }

}
