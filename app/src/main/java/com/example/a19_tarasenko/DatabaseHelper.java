package com.example.a19_tarasenko;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userstore.db";
    private static final int SCHEMA = 2; // Версия базы данных (увеличили до 2 из-за новых полей)
    static final String TABLE = "users";

    // Названия колонок в базе данных
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "year";

    // ДОБАВЛЕННЫЕ ПОЛЯ ДЛЯ ЗАДАНИЯ 4:
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Команда на создание таблицы со всеми 5 колонками
        db.execSQL("CREATE TABLE users (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_YEAR + " INTEGER, "
                + COLUMN_EMAIL + " TEXT, " + COLUMN_PHONE + " TEXT);");

        // Добавляем тестового пользователя при первом запуске
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_YEAR + ", " + COLUMN_EMAIL + ", " + COLUMN_PHONE
                + ") VALUES ('Том Смит', 1981, 'tom@example.com', '+79991234567');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}