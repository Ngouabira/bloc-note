package cg.rcksoft.app.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cg.rcksoft.data.DaoMaster;
import cg.rcksoft.data.DaoSession;
import cg.rcksoft.data.EventDao;
import cg.rcksoft.data.NoteDao;

/**
 * Created by Ricken BAZOLO on 09/08/2016.
 */
public class AppHelper {

    private DaoSession daoSession;
    private NoteDao noteDao;
    private EventDao eventDao;
    Context myContex;


    public AppHelper(Context myContex) {
        /**
         * Create and open data base once
         */
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(myContex, "notedb-rcksoft", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * To Manage Entity Note
     * @return
     */
    public NoteDao getNoteDao(){
        return (noteDao = daoSession.getNoteDao());
    }

    /**
     * To Manage Entity Event
     * @return
     */
    public EventDao getEventDao(){
        return (eventDao = daoSession.getEventDao());
    }

}
