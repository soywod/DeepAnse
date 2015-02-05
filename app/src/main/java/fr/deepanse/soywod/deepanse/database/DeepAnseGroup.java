package fr.deepanse.soywod.deepanse.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

import fr.deepanse.soywod.deepanse.Conversion;

/**
 * Created by soywod on 05/02/2015.
 */
public class DeepAnseGroup {

    private DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    /**
     *  Constructeur DeepAnse standard
     *
     *  @param deepAnseSQLiteOpenHelper  DeepAnseSQLiteOpenHelper
     */
    public DeepAnseGroup(DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper) {
        this.deepAnseSQLiteOpenHelper = deepAnseSQLiteOpenHelper;
    }

    public void open() throws SQLException {
        sqLiteDatabase = deepAnseSQLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        deepAnseSQLiteOpenHelper.close();
    }

    /**
     *  Ajoute une rubrique en BDD
     *
     *  @param group     La rubrique à ajouter en BDD de type DeepAnseGroup
     *
     *  @return
     *      L'id de la nouvelle rubrique ajoutée en BDD de type long
     */
    public long insert(fr.deepanse.soywod.deepanse.model.DeepAnseGroup group) {
        ContentValues values = new ContentValues();

        values.put(DeepAnseSQLiteOpenHelper.NAME, group.getName());

        return sqLiteDatabase.insert(DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP, null, values);
    }

    /**
     *  Modifie une rubrique en BDD
     *
     *  @param id           L'id de la rubrique à modifier en BDD de type long
     *  @param group     La rubrique à modifier en BDD de type DeepAnseGroup
     *
     *  @return
     *      Le code de retour de type int
     */
    public int update(long id, fr.deepanse.soywod.deepanse.model.DeepAnseGroup group) {
        ContentValues values = new ContentValues();

        values.put(DeepAnseSQLiteOpenHelper.NAME, group.getName());

        return sqLiteDatabase.update(DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP, values, DeepAnseSQLiteOpenHelper.ID + " = " + id, null);
    }

    /**
     *  Supprime une rubrique de la BDD
     *
     *  @param id   L'id de la rubrique à supprimer de la BDD de type long
     *
     *  @return
     *      Le code de retour de type int
     */
    public int delete(long id) {
        return sqLiteDatabase.delete(DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP, DeepAnseSQLiteOpenHelper.ID + " = " + id, null);
    }

    /**
     *  Sélectionne une rubrique de la BDD
     *
     *  @param id   L'id de la rubrique de la BDD de type long
     *
     *  @return
     *      La rubrique sélectionnée de la BDD de type DeepAnseGroup
     */
    public fr.deepanse.soywod.deepanse.model.DeepAnseGroup select(long id) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP +" WHERE " + DeepAnseSQLiteOpenHelper.ID + " = ?" , new String[]{String.valueOf(id)});
        fr.deepanse.soywod.deepanse.model.DeepAnseGroup group;

        cursor.moveToFirst();

        group = Conversion.cursorToDeepAnseGroup(cursor);

        cursor.close();

        return group;
    }

    /**
     *  Sélectionne toutes les rubriques de la BDD
     *
     *  @return
     *      La liste des rubriques de la BDD de type ArrayList
     */
    public ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> selectAll()
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP, null);
        ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnseGroup> arrayGroup = new ArrayList<>();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            arrayGroup.add(Conversion.cursorToDeepAnseGroup(cursor));

        cursor.close();

        return arrayGroup;
    }
}