package cg.rcksoft.views.listeners;

import android.view.View;

/**
 * Created by Ricken BAZOLO on 5/19/2017.
 */

public interface NoteItemListener {

    void onNoteItemClick(int p, View v);

    void onNoteItemLongClick(int p, View v);

    void onNoteFavoriteClick(int p, View v);

    void onNoteFavoriteLongClick(int p, View v);
}
