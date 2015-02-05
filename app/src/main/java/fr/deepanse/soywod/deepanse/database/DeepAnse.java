package fr.deepanse.soywod.deepanse.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import fr.deepanse.soywod.deepanse.Conversion;

/**
 * Created by soywod on 05/02/2015.
 */
public class DeepAnse {

    private DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    /**
     *  Constructeur DeepAnse standard
     *
     *  @param deepAnseSQLiteOpenHelper  DeepAnseSQLiteOpenHelper
     */
    public DeepAnse(DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper) {
        this.deepAnseSQLiteOpenHelper = deepAnseSQLiteOpenHelper;
    }

    public void open() throws SQLException {
        sqLiteDatabase = deepAnseSQLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        deepAnseSQLiteOpenHelper.close();
    }

    /**
     *  Ajoute une dépense en BDD
     *
     *  @param deepAnse     La dépense à ajouter en BDD de type DeepAnse
     *
     *  @return
     *      L'id de la nouvelle dépense ajoutée en BDD de type long
     */
    public long insert(fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse) {
        ContentValues values = new ContentValues();

        values.put(DeepAnseSQLiteOpenHelper.AMOUNT, deepAnse.getAmount());
        values.put(DeepAnseSQLiteOpenHelper.DATE, Conversion.dateToString(deepAnse.getDate()));
        values.put(DeepAnseSQLiteOpenHelper.ID_GROUP, deepAnse.getGroup().getId());
        values.put(DeepAnseSQLiteOpenHelper.COMMENT, deepAnse.getComment());
        values.put(DeepAnseSQLiteOpenHelper.RECURSIVE, deepAnse.isRecursive());

        return sqLiteDatabase.insert(DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE, null, values);
    }

    /**
     *  Modifie une dépense en BDD
     *
     *  @param id           L'id de la dépense à modifier en BDD de type long
     *  @param deepAnse     La dépense à modifier en BDD de type DeepAnse
     *
     *  @return
     *      Le code de retour de type int
     */
    public int update(long id, fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse) {
        ContentValues values = new ContentValues();

        values.put(DeepAnseSQLiteOpenHelper.AMOUNT, deepAnse.getAmount());
        values.put(DeepAnseSQLiteOpenHelper.DATE, Conversion.dateToString(deepAnse.getDate()));
        values.put(DeepAnseSQLiteOpenHelper.ID_GROUP, deepAnse.getGroup().getId());
        values.put(DeepAnseSQLiteOpenHelper.COMMENT, deepAnse.getComment());
        values.put(DeepAnseSQLiteOpenHelper.RECURSIVE, deepAnse.isRecursive());

        return sqLiteDatabase.update(DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE, values, DeepAnseSQLiteOpenHelper.ID + " = " + id, null);
    }

    /**
     *  Supprime une dépense de la BDD
     *
     *  @param id   L'id de la dépense à supprimer de la BDD de type long
     *
     *  @return
     *      Le code de retour de type int
     */
    public int delete(long id) {
        return sqLiteDatabase.delete(DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE, DeepAnseSQLiteOpenHelper.ID + " = " + id, null);
    }

    /**
     *  Sélectionne une dépense de la BDD
     *
     *  @param id   L'id de la dépense de la BDD de type long
     *
     *  @return
     *      La dépense sélectionnée de la BDD de type DeepAnse
     */
    public fr.deepanse.soywod.deepanse.model.DeepAnse select(long id) {
        Cursor cursorDeepAnse = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE +" WHERE " + DeepAnseSQLiteOpenHelper.ID + " = ?" , new String[]{String.valueOf(id)});
        fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse;

        cursorDeepAnse.moveToFirst();

        Cursor cursorGroup = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP +" WHERE " + DeepAnseSQLiteOpenHelper.ID + " = ?" , new String[]{String.valueOf(cursorDeepAnse.getInt(3))});

        cursorGroup.moveToFirst();

        deepAnse = Conversion.cursorToDeepAnse(cursorDeepAnse, Conversion.cursorToDeepAnseGroup(cursorGroup));

        cursorGroup.close();
        cursorDeepAnse.close();

        return deepAnse;
    }
}