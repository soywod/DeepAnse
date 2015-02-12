package fr.deepanse.soywod.deepanse.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import fr.deepanse.soywod.deepanse.model.Conversion;

/**
 * Created by soywod on 05/02/2015.
 */
public class DeepAnse {

    private DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    /**
     *  Constructeur ReportByDay standard
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
     *  @param deepAnse     La dépense à ajouter en BDD de type ReportByDay
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
     *  @param deepAnse     La dépense à modifier en BDD de type ReportByDay
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
     *      La dépense sélectionnée de la BDD de type ReportByDay
     */
    public fr.deepanse.soywod.deepanse.model.DeepAnse select(long id) {
        Cursor cursorDeepAnse = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE +" WHERE " + DeepAnseSQLiteOpenHelper.ID + " = ?" , new String[]{String.valueOf(id)});

        if (cursorDeepAnse.getCount() != 0) {
            fr.deepanse.soywod.deepanse.model.DeepAnse deepAnse;

            cursorDeepAnse.moveToFirst();
            Cursor cursorGroup = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP +" WHERE " + DeepAnseSQLiteOpenHelper.ID + " = ?" , new String[]{String.valueOf(cursorDeepAnse.getInt(3))});
            cursorGroup.moveToFirst();
            deepAnse = Conversion.cursorToDeepAnse(cursorDeepAnse, Conversion.cursorToDeepAnseGroup(cursorGroup));

            cursorGroup.close();
            cursorDeepAnse.close();

            return deepAnse;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> selectAll()
    {
        Cursor cursorDeepAnse = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE, null);

        if (cursorDeepAnse.getCount() != 0) {
            ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> arrayDeepAnse = new ArrayList<>();

            for(cursorDeepAnse.moveToFirst(); !cursorDeepAnse.isAfterLast(); cursorDeepAnse.moveToNext())
            {
                Cursor cursorGroup = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP +" WHERE " + DeepAnseSQLiteOpenHelper.ID + " = ?" , new String[]{String.valueOf(cursorDeepAnse.getInt(3))});

                cursorGroup.moveToFirst();
                arrayDeepAnse.add(Conversion.cursorToDeepAnse(cursorDeepAnse, Conversion.cursorToDeepAnseGroup(cursorGroup)));
                cursorGroup.close();
            }
            cursorDeepAnse.close();

            return arrayDeepAnse;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @param date     La date de la dépense de la BDD de type GregorianCalendar
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<fr.deepanse.soywod.deepanse.model.Report> selectAllByYear(GregorianCalendar date)
    {
        Cursor cursorReport = sqLiteDatabase.rawQuery("SELECT strftime('%m', "+DeepAnseSQLiteOpenHelper.DATE+") AS month, strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") as year, SUM("+DeepAnseSQLiteOpenHelper.AMOUNT+") as sum FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE year = '" + date.get(GregorianCalendar.YEAR) + "' " +
                "GROUP BY month", null);

        if (cursorReport.getCount() != 0) {
            ArrayList<fr.deepanse.soywod.deepanse.model.Report> arrayReport = new ArrayList<>();

            for(cursorReport.moveToFirst(); !cursorReport.isAfterLast(); cursorReport.moveToNext())
                arrayReport.add(Conversion.cursorToReportByYear(cursorReport));
            cursorReport.close();

            return arrayReport;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @param date     La date de la dépense de la BDD de type GregorianCalendar
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<fr.deepanse.soywod.deepanse.model.Report> selectAllByMonth(GregorianCalendar date)
    {
        Cursor cursorReport = sqLiteDatabase.rawQuery("SELECT strftime('%d', "+DeepAnseSQLiteOpenHelper.DATE+") AS day, strftime('%m', "+DeepAnseSQLiteOpenHelper.DATE+") AS month, strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") as year, SUM("+DeepAnseSQLiteOpenHelper.AMOUNT+") as sum FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE year = '" + date.get(GregorianCalendar.YEAR) + "' " +
                "AND month = '" + ((date.get(GregorianCalendar.MONTH) < 9)?("0" + (date.get(GregorianCalendar.MONTH)+1)):("" + (date.get(GregorianCalendar.MONTH)+1))) + "' " +
                "GROUP BY day", null);

        if (cursorReport.getCount() != 0) {
            ArrayList<fr.deepanse.soywod.deepanse.model.Report> arrayReport = new ArrayList<>();

            for(cursorReport.moveToFirst(); !cursorReport.isAfterLast(); cursorReport.moveToNext())
                arrayReport.add(Conversion.cursorToReportByMonth(cursorReport));
            cursorReport.close();

            return arrayReport;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @param date     La date de la dépense de la BDD de type GregorianCalendar
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> selectAllByDay(GregorianCalendar date)
    {
        Cursor cursorDeepAnse = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + date.get(GregorianCalendar.YEAR) + "' " +
                "AND strftime('%m', "+DeepAnseSQLiteOpenHelper.DATE+") = '"+((date.get(GregorianCalendar.MONTH) < 9)?("0" + (date.get(GregorianCalendar.MONTH)+1)):("" + (date.get(GregorianCalendar.MONTH)+1)))+"' " +
                "AND strftime('%d', "+DeepAnseSQLiteOpenHelper.DATE+") = '"+date.get(GregorianCalendar.DAY_OF_MONTH)+"'", null);

        if (cursorDeepAnse.getCount() != 0) {
            ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> arrayDeepAnse = new ArrayList<>();

            for(cursorDeepAnse.moveToFirst(); !cursorDeepAnse.isAfterLast(); cursorDeepAnse.moveToNext())
            {
                Cursor cursorGroup = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP +" WHERE " + DeepAnseSQLiteOpenHelper.ID + " = ?" , new String[]{String.valueOf(cursorDeepAnse.getInt(3))});
                cursorGroup.moveToFirst();
                arrayDeepAnse.add(Conversion.cursorToDeepAnse(cursorDeepAnse, Conversion.cursorToDeepAnseGroup(cursorGroup)));
                cursorGroup.close();
            }
            cursorDeepAnse.close();

            return arrayDeepAnse;
        }
        else {
            return null;
        }
    }
}
