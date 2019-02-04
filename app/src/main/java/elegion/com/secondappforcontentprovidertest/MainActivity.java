package elegion.com.secondappforcontentprovidertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
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
    private Spinner tables_sp;//--определяем -Spinner tables_sp
    private Spinner query_sp;//------еще Спиннер с вариантами запроса
    private Button mQueryBtn;
    private EditText etId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tables_sp =findViewById(R.id.tables_sp);
        query_sp = findViewById(R.id.query_sp);
        etId =findViewById(R.id.et_id);


//TODO Перенести OnClick из метода OnCreate
        //TODO реализовать в пернесенном методе логику работы согласно ТЗ

        mQueryBtn = (Button)findViewById(R.id.QueryBtn);
        mQueryBtn.setOnClickListener(this);

        getSupportLoaderManager().initLoader(1, null, this);

    }

    @Override
    public void onClick(View v) {

        String tables_sp_selected= tables_sp.getSelectedItem().toString();
        Toast.makeText(getApplicationContext(), tables_sp_selected, Toast.LENGTH_SHORT).show();
        String query_sp_selected= query_sp.getSelectedItem().toString();
        Toast.makeText(getApplicationContext(), query_sp_selected, Toast.LENGTH_SHORT).show();
        //выбираем действие в зависимости от типа запроса
        if(query_sp_selected.toLowerCase().equals("query")) {
            Toast.makeText(getApplicationContext(), "selected query", Toast.LENGTH_SHORT).show();
        }
        if(query_sp_selected.toLowerCase().equals("insert")) {
            Toast.makeText(getApplicationContext(), "selected insert", Toast.LENGTH_SHORT).show();
        }

        if(query_sp_selected.toLowerCase().equals("update")){
            Toast.makeText(getApplicationContext(),"selected update",Toast.LENGTH_SHORT).show();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", 0);
            contentValues.put("name", "new Name");
            contentValues.put("release", "tomorrow");
            getContentResolver().update(Uri.parse("content://com.elegion.roomdatabase.musicprovider/album/1"), contentValues, null, null);


        }

        if(query_sp_selected.toLowerCase().equals("delete")){
            Toast.makeText(getApplicationContext(),"selected delete",Toast.LENGTH_SHORT).show();
        }




    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Uri.parse("content://com.elegion.roomdatabase.musicprovider/album/"),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            StringBuilder builder = new StringBuilder();
            do {
                builder.append(data.getString(data.getColumnIndex("name"))).append("\n");
            } while (data.moveToNext());
            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}