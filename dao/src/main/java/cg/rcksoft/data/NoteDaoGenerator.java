package cg.rcksoft.data;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class NoteDaoGenerator {

    public static void main(String args[]) throws Exception {

        //create schema
        Schema schema = new Schema(2, "cg.rcksoft.data");

        //create entity
        Entity event = schema.addEntity("event");
        event.addIdProperty().primaryKey().autoincrement().columnName("eventID");
        event.addStringProperty("events");
        event.addDateProperty("dateEditEvent");
        event.addDateProperty("dateAlertEvent");
        event.addDateProperty("heureAlertEvent");

        Entity note = schema.addEntity("note");
        note.addIdProperty().primaryKey().autoincrement().columnName("noteID");
        note.addStringProperty("title");
        note.addStringProperty("description");
        note.addDateProperty("dateEditNote");
        note.addIntProperty("flagFavoriteNote");
        /*note.addStringProperty("dateAlert");
        note.addStringProperty("heureAlert");
        note.addToOneWithoutProperty("alarm", event, "fkEvent");*/

        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }
}
