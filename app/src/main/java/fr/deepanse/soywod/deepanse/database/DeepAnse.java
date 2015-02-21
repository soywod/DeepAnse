package fr.deepanse.soywod.deepanse.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Objects;

import fr.deepanse.soywod.deepanse.model.*;

/**
 * Created by soywod on 05/02/2015.
 */
public class DeepAnse {

    private DeepAnseSQLiteOpenHelper deepAnseSQLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    /**
     *  Constructeur ViewByDay standard
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
     *  @param deepAnse     La dépense à ajouter en BDD de type ViewByDay
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
     *  @param deepAnse     La dépense à modifier en BDD de type ViewByDay
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
     *      La dépense sélectionnée de la BDD de type ViewByDay
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

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<Object[]> selectAllYear()
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") AS year, SUM("+DeepAnseSQLiteOpenHelper.AMOUNT+") FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "GROUP BY year", null);

        if (cursor.getCount() != 0) {
            ArrayList<Object[]> array = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                array.add(new Object[]{cursor.getString(0), cursor.getDouble(1)});
            cursor.close();

            return array;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<String> selectAllYearWithOutSum()
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") AS year FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "GROUP BY year " +
                "ORDER BY year DESC", null);

        if (cursor.getCount() != 0) {
            ArrayList<String> array = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                array.add(cursor.getString(0));
            cursor.close();

            return array;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<String> selectAllMonth(String year)
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%m', " + DeepAnseSQLiteOpenHelper.DATE + ") AS month FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + year + "' " +
                "GROUP BY month " +
                "ORDER BY month DESC", null);

        if (cursor.getCount() != 0) {
            ArrayList<String> array = new ArrayList<>();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                array.add(Conversion.firstCharToUpperCase(DateFR.findDateLitteral(cursor.getInt(0)-1)));
            cursor.close();

            return array;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<Object[]> selectYearSumByGroup(Object year)
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") AS year, "+DeepAnseSQLiteOpenHelper.ID_GROUP+" AS id_group, SUM("+DeepAnseSQLiteOpenHelper.AMOUNT+") FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + year + "' " +
                "GROUP BY year, id_group", null);

        if (cursor.getCount() != 0) {
            ArrayList<Object[]> array = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                array.add(new Object[]{cursor.getString(0), cursor.getLong(1), cursor.getDouble(2)});
            cursor.close();

            return array;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<Object[]> selectAllMonthByYear(Object year)
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%m', " + DeepAnseSQLiteOpenHelper.DATE + ") AS month, SUM("+DeepAnseSQLiteOpenHelper.AMOUNT+") FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + year + "' " +
                "GROUP BY month", null);

        if (cursor.getCount() != 0) {
            ArrayList<Object[]> array = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                array.add(new Object[]{cursor.getString(0), cursor.getDouble(1)});
            cursor.close();

            return array;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<Object[]> selectMonthSumByGroup(Object year, Object month)
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%m', " + DeepAnseSQLiteOpenHelper.DATE + ") AS month, "+DeepAnseSQLiteOpenHelper.ID_GROUP+" AS id_group, SUM("+DeepAnseSQLiteOpenHelper.AMOUNT+") FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + year + "' " +
                "AND strftime('%m', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + month + "' " +
                "GROUP BY month, id_group", null);

        if (cursor.getCount() != 0) {
            ArrayList<Object[]> array = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                array.add(new Object[]{cursor.getString(0), cursor.getLong(1), cursor.getDouble(2)});
            cursor.close();

            return array;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<Object[]> selectAllDayByYearMonth(Object year, Object month)
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%d', " + DeepAnseSQLiteOpenHelper.DATE + ") AS day, SUM("+DeepAnseSQLiteOpenHelper.AMOUNT+") FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + year + "' " +
                "AND strftime('%m', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + month + "' " +
                "GROUP BY day", null);

        if (cursor.getCount() != 0) {
            ArrayList<Object[]> array = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                array.add(new Object[]{cursor.getString(0), cursor.getDouble(1)});
            cursor.close();

            return array;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<Object[]> selectDaySumByGroup(Object year, Object month, Object day)
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT strftime('%d', " + DeepAnseSQLiteOpenHelper.DATE + ") AS day, "+DeepAnseSQLiteOpenHelper.ID_GROUP+" AS id_group, SUM("+DeepAnseSQLiteOpenHelper.AMOUNT+") FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + year + "' " +
                "AND strftime('%m', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + month + "' " +
                "AND strftime('%d', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + day + "' " +
                "GROUP BY day, id_group", null);

        if (cursor.getCount() != 0) {
            ArrayList<Object[]> array = new ArrayList<>();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                array.add(new Object[]{cursor.getString(0), cursor.getLong(1), cursor.getDouble(2)});
            cursor.close();

            return array;
        }
        else {
            return null;
        }
    }

    /**
     *  Sélectionne toutes les dépenses de la BDD du mois et année donnés en paramètre
     *
     *  @return
     *      La liste des dépenses de la BDD de type ArrayList
     */
    public ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> selectAllByDateGroup(GregorianCalendar date, fr.deepanse.soywod.deepanse.model.DeepAnseGroup group)
    {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE + " " +
                "WHERE strftime('%Y', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + date.get(GregorianCalendar.YEAR) + "' " +
                "AND strftime('%m', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + ((date.get(GregorianCalendar.MONTH) < 9)?("0" + (date.get(GregorianCalendar.MONTH)+1)):("" + (date.get(GregorianCalendar.MONTH)+1))) + "' " +
                "AND strftime('%d', " + DeepAnseSQLiteOpenHelper.DATE + ") = '" + ((date.get(GregorianCalendar.DAY_OF_MONTH) < 10)?("0" + date.get(GregorianCalendar.DAY_OF_MONTH)):("" + date.get(GregorianCalendar.DAY_OF_MONTH))) + "' " +
                "AND " + DeepAnseSQLiteOpenHelper.ID_GROUP + " = " + group.getId() + " " +
                "ORDER BY " + DeepAnseSQLiteOpenHelper.AMOUNT, null);

        if (cursor.getCount() != 0) {
            ArrayList<fr.deepanse.soywod.deepanse.model.DeepAnse> arrayDeepAnse = new ArrayList<>();

            Cursor cursorGroup = sqLiteDatabase.rawQuery("SELECT * FROM " + DeepAnseSQLiteOpenHelper.TABLE_DEEPANSE_GROUP +" WHERE " + DeepAnseSQLiteOpenHelper.ID + " = " + group.getId(), null);
            cursorGroup.moveToFirst();

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                arrayDeepAnse.add(Conversion.cursorToDeepAnse(cursor, Conversion.cursorToDeepAnseGroup(cursorGroup)));

            cursorGroup.close();
            cursor.close();

            return arrayDeepAnse;
        }
        else {
            return null;
        }
    }
}
