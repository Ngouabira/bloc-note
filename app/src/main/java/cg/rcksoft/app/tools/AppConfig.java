package cg.rcksoft.app.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import cg.rcksoft.data.DaoMaster;
import cg.rcksoft.data.DaoSession;
import cg.rcksoft.data.Event;
import cg.rcksoft.data.EventDao;
import cg.rcksoft.data.Note;
import cg.rcksoft.data.NoteDao;

/**
 * Created by Ricken BAZOLO on 09/08/2016.
 */
public class AppConfig {

    private static DaoSession daoSession;
    private static DaoMaster.DevOpenHelper helper;
    Context myContex;


    /**
     * Create data base
     */
    public AppConfig(Context myContex) {

        helper = new DaoMaster.DevOpenHelper(myContex, "notedb-rcksoft", null);
    }

    /**
     *
     */
    public static Object getWritableDatabase(Class<?> obj){
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
    public static Object getReadableDatabase(Class<?> obj){
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
    private static NoteDao getNoteDao(){
        return (daoSession.getNoteDao());
    }

    /**
     * To Manage Entity Event
     * @return
     */
    private static EventDao getEventDao(){
        return (daoSession.getEventDao());
    }

    /**
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date){
        SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simple.format(date);
    }
}
