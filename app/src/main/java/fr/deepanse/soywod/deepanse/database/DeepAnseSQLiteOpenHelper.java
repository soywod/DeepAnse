package fr.deepanse.soywod.deepanse.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by soywod on 05/02/2015.
 */
public class DeepAnseSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ReportByDay.db";
    public static final int VERSION = 7;

    public static final String AMOUNT = "amount";
    public static final String COLOR = "color";
    public static final String COMMENT = "comment";
    public static final String DATE = "date";
    public static final String ID = "_id";
    public static final String ID_GROUP = "id_group";
    public static final String NAME = "name";
    public static final String RECURSIVE = "recursive";

    public static final String TABLE_DEEPANSE = "deepanse";
    public static final String TABLE_DEEPANSE_GROUP = "deepanse_group";

    public static final String CREATE_TABLE_DEEPANSE = "CREATE TABLE IF NOT EXISTS " + TABLE_DEEPANSE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AMOUNT + " REAL NOT NULL, " +
            DATE + " DATE NOT NULL, " +
            ID_GROUP + " INTEGER NOT NULL REFERENCES " + TABLE_DEEPANSE_GROUP + "(" + ID + "), " +
            COMMENT + " TEXT, " +
            RECURSIVE + " INTEGER);";

    public static final String CREATE_TABLE_DEEPANSE_GROUP = "CREATE TABLE IF NOT EXISTS " + TABLE_DEEPANSE_GROUP + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT UNIQUE NOT NULL, " +
            COLOR + " INTEGER);";

    public DeepAnseSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_DEEPANSE_GROUP);
        database.execSQL(CREATE_TABLE_DEEPANSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_DEEPANSE);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_DEEPANSE_GROUP);

        onCreate(database);
    }
}
