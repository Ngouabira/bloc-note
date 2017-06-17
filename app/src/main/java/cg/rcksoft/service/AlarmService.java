package cg.rcksoft.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Ricken BAZOLO on 6/15/2017.
 */

public class AlarmService extends IntentService {


    public AlarmService() {
        super("cg.rcksoft.notes.AlarmService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
