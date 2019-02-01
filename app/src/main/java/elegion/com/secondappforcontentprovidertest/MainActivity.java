package elegion.com.secondappforcontentprovidertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
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

    Spinner tables_sp =findViewById(R.id.tables_sp); //--определяем -Spinner tables_sp
    Spinner query_sp = findViewById(R.id.query_sp); //------еще Спиннер с вариантами запроса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настраиваем адаптеры
        ArrayAdapter<?> tables_adapter =
                ArrayAdapter.createFromResource(this, R.array.tableslist, android.R.layout.simple_spinner_item);
        tables_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<?> querytype_adapter =
                ArrayAdapter.createFromResource(this, R.array.querytypelist, android.R.layout.simple_spinner_item);
        querytype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Настраиваем адаптер

        findViewById(R.id.QueryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 0);
                contentValues.put("name", "new Name");
                contentValues.put("release", "tomorrow");
                getContentResolver().update(Uri.parse("content://com.elegion.roomdatabase.musicprovider/album/1"), contentValues, null, null);

            }
        });

        getSupportLoaderManager().initLoader(1, null, this);

    }


    //------здесь процедуры для визуального составления запроса
    /*
    String selected = spinner.getSelectedItem().toString();
Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();*/

    String selected= tables_sp.getSelectedItem().toString();



    //------конец процедуры для визуального составления допроса
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Uri.parse("content://com.elegion.roomdatabase.musicprovider/album"),
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
