package elegion.com.secondappforcontentprovidertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String CONTENT = "content://";
    private static final String AUTHORITY = "com.elegion.roomdatabase.musicprovider";
    private static final String TABLE_ALBUM = "album";
    private static final String TABLE_SONG ="song" ;
    private static final String TABLE_ALBUMSONG = "album_song";

    private static final int NONE_ID = -1;
    /*
    
                В проекте SecondAppForContentProviderTestSecondAppForContentProviderTest определить вертикальный LinearLayout.---
    
                Добавить на разметку 2 спиннера  ----
               -  в одном “Albums”, “Songs”, "AlbumSongs"
                        -  во втором - “query”, “insert”, “update”, “delete”; Строки захардкожены.
    
                Добавить EditText, в который в последствии будет вписываться id элемента.
                Если EditText пустой, то валидным считается только “query” и “insert”, на методы delete() и update() показывать тост с ошибкой.
                Добавить Button (далее - кнопка действия), при нажатии на который будет выполняться выбранное во втором спиннере
                действие с выбранной в первом спиннере таблицей и вписанным id в первый EditText.
                Действие реализовать с помощью соответствующих методов getContentResolver() (query() / insert() / update() / delete(), пример есть в проекте)
                Если в EditText (id) вписано не число или число, но записи под таким id в таблице нет, то при нажатии выводить тост - “Ошибка id”
                Добавить три EditText’а (далее - EditText c данными), из которых будут заполнятся данные ContentValues для методов insert() и update()
                Проверка валидности происходит с помощью метода query(), который должен показывать тост с текущими данными в выбранной таблице.
            */
    private Spinner mTableSp;//--определяем -Spinner mTableSp
    private Spinner mCommandSp;//------еще Спиннер с вариантами запроса
    private Button mQueryBtn;

    private EditText mEditTextId;
    private EditText mEditTextName;
    private EditText mEditTextRelease;
    private EditText mEditTextDuration;

    private String tables_sp_selected;
    private Uri uri;

    private static final String LOG_TAG ="SecondApp" ;
    private ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTableSp =findViewById(R.id.tables_sp);
        mCommandSp = findViewById(R.id.command_sp);

        mEditTextId =findViewById(R.id.et_id);
        mEditTextName =findViewById(R.id.et_name);
        mEditTextRelease =findViewById(R.id.et_release);
        mEditTextDuration =findViewById(R.id.et_duration);

        mQueryBtn = (Button)findViewById(R.id.QueryBtn);
        mQueryBtn.setOnClickListener(this);

    }




    @Override
    public void onClick(View v) {

        tables_sp_selected= mTableSp.getSelectedItem().toString();
        String id = mEditTextId.getText().toString();
        String table = mTableSp.getSelectedItem().toString();
        String command = mCommandSp.getSelectedItem().toString();
        boolean empty = ismEditTextIdEmpty(id);

        if (empty && (command.equals("delete") || command.equals("update"))) {
            Toast.makeText(MainActivity.this,
                    "Нельзя использовать команды delete и update с пустым id",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            executeCommand(getTableName(table), command, convertToInt(id));
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Ошибка id", Toast.LENGTH_SHORT).show();
        }

    }

    private void executeCommand(String tableName, String command, int id) {

        switch(command){
            case "query" :
                query(tableName,id);
            case "insert" :
                insert(tableName,id);
            case "update":
                update(tableName, id);
            case "delete":
                delete(tableName,id);
            default:
        }

    }


    private void query(String tableName, int id) {
        StringBuilder builder = new StringBuilder();
        if (id != NONE_ID) {
            Cursor cursor = getContentResolver().query(Uri.parse(CONTENT + AUTHORITY + "/" + tableName + "/" + id),
                    null,
                    null,
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames) {
                    builder.append(cursor.getString(cursor.getColumnIndex(columnName))).append(" ");
                }
                Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Ошибка id", Toast.LENGTH_LONG).show();
            }

        } else {
            Cursor cursor = getContentResolver().query(Uri.parse(CONTENT + AUTHORITY + "/" + tableName),
                    null,
                    null,
                    null,
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String[] columnNames = cursor.getColumnNames();
                    for (String columnName : columnNames) {
                        builder.append(cursor.getString(cursor.getColumnIndex(columnName))).append(" ");
                    }
                    builder.append("\n");
                } while (cursor.moveToNext());
            }
            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
        }

    }
    private void insert(String tableName, int id) {
        if (TABLE_ALBUM.equals(tableName)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", mEditTextId.getText().toString());
            contentValues.put("name", mEditTextName.getText().toString());
            contentValues.put("release", mEditTextRelease.getText().toString());
            getContentResolver().insert(Uri.parse(CONTENT + AUTHORITY + "/" + tableName), contentValues);
        } else if (TABLE_SONG.equals(tableName)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", mEditTextId.getText().toString());
            contentValues.put("name", mEditTextName.getText().toString());
            contentValues.put("duration", mEditTextDuration.getText().toString());
            getContentResolver().insert(Uri.parse(CONTENT + AUTHORITY + "/" + tableName), contentValues);
        } else if (TABLE_ALBUMSONG.equals(tableName)) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("album_id", mEditTextName.getText().toString());
                contentValues.put("song_id", mEditTextDuration.getText().toString());
                getContentResolver().insert(Uri.parse(CONTENT + AUTHORITY + "/" + tableName), contentValues);
            } catch (SQLiteConstraintException ex){
                Toast.makeText(this,
                        "Ошибка id. Вы пытаетесь добавить запись, для которой нет существующих FOREIGN KEY",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    private void update(String tableName, int id) {
    }
    private void delete(String tableName, int id) {
    }

    private String getTableName(String table) {
        switch (table) {
            case "Albums":
                return TABLE_ALBUM;
            case "Songs":
                return TABLE_SONG;
            case "AlbumSongs":
                return TABLE_ALBUMSONG;
            default:
                return "";
        }
    }

    private int convertToInt(String id) throws NumberFormatException {
        if (!ismEditTextIdEmpty(id)) {
            return Integer.parseInt(id);
        }
        return NONE_ID;
    }

    private boolean ismEditTextIdEmpty(String id) {
        return TextUtils.isEmpty(id);
    }


}