package cg.rcksoft.app.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cg.rcksoft.data.DaoMaster;
import cg.rcksoft.data.DaoSession;
import cg.rcksoft.data.Event;
import cg.rcksoft.data.EventDao;
import cg.rcksoft.data.Note;
import cg.rcksoft.data.NoteDao;

/**
 * Created by Ricken BAZOLO on 09/08/2016.
 */
public class AppHelper {

    private DaoSession daoSession;
    //private NoteDao noteDao;
    //private EventDao eventDao;
    private DaoMaster.DevOpenHelper helper;
    Context myContex;


    /**
     * Create data base
     */
    public AppHelper(Context myContex) {

        helper = new DaoMaster.DevOpenHelper(myContex, "notedb-rcksoft", null);
    }

    /**
     *
     */
    public Object getWritableDatabase(Class<?> obj){
        Object object = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        if( obj.isInstance(new Note())){
            object = getNoteDao();
        }else if(obj.isInstance(new Event())){
            object = getEventDao();
        }
        return object;
    }

    /**
     *
     */
    public Object getReadableDatabase(Class<?> obj){
        Object object = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        if( obj.isInstance(new Note())){
            object = getNoteDao();
        }else if(obj.isInstance(new Event())){
            object = getEventDao();
        }
        return object;
    }

    /**
     * To Manage Entity Note
     * @return
     */
    private NoteDao getNoteDao(){
        return (daoSession.getNoteDao());
    }

    /**
     * To Manage Entity Event
     * @return
     */
    private EventDao getEventDao(){
        return (daoSession.getEventDao());
    }

}
