package cg.rcksoft.data;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class NoteDaoGenerator {

    public static void main(String args[]) throws Exception {

        //create schema
        Schema schema = new Schema(1, "cg.rcksoft.data");

        //create entity
        Entity note = schema.addEntity("note");
        note.addIdProperty().primaryKey().autoincrement();
        note.addStringProperty("title");
        note.addStringProperty("description");
        note.addDateProperty("dateEditNote");

        Entity event = schema.addEntity("event");
        event.addIdProperty().primaryKey().autoincrement();
        event.addStringProperty("events");
        event.addDateProperty("dateEditEvent");
        event.addDateProperty("dateAlertEvent");
        event.addDateProperty("heureAlertEvent");

        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }
}
