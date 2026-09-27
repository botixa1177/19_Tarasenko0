package com.example.a19_tarasenko;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {
    EditText nameBox, yearBox, emailBox, phoneBox;
    Button delButton, saveButton;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Связываем переменные с элементами интерфейса, которые мы создали в XML
        nameBox = findViewById(R.id.name);
        yearBox = findViewById(R.id.year);
        emailBox = findViewById(R.id.email);
        phoneBox = findViewById(R.id.phone);
        delButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        // Проверяем, передали ли нам ID (если да - это редактирование старого, если нет - создание нового)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (userId > 0) {
            // Загружаем данные из базы
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();

            // Вставляем данные в текстовые поля
            nameBox.setText(userCursor.getString(1));
            yearBox.setText(String.valueOf(userCursor.getInt(2)));
            emailBox.setText(userCursor.getString(3)); // Читаем email (колонка 3)
            phoneBox.setText(userCursor.getString(4)); // Читаем телефон (колонка 4)
            userCursor.close();
        } else {
            // Если мы создаем нового пользователя, кнопка "Удалить" не нужна
            delButton.setVisibility(View.GONE);
        }
    }

    // Метод сохранения
    public void save(View view) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_EMAIL, emailBox.getText().toString()); // Сохраняем email
        cv.put(DatabaseHelper.COLUMN_PHONE, phoneBox.getText().toString()); // Сохраняем телефон

        if (userId > 0) {
            // Обновляем существующую запись
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + userId, null);
        } else {
            // Добавляем новую
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        goHome();
    }

    // Метод удаления
    public void delete(View view) {
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }

    // Возврат на главный экран
    private void goHome() {
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}