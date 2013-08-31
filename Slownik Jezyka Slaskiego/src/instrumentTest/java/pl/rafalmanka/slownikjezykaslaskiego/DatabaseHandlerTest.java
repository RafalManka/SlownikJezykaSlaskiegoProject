package pl.rafalmanka.slownikjezykaslaskiego;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

/**
 * Created by rafal on 8/23/13.
 */

public class DatabaseHandlerTest extends ActivityInstrumentationTestCase2<SearchDictionary>{

    public static final String TAG = DatabaseHandlerTest.class.getSimpleName();
    private Solo mSolo;

    public DatabaseHandlerTest() {
        super(SearchDictionary.class);
    }


    public void setUp() throws Exception {
        mSolo = new Solo( getInstrumentation(), getActivity() );
    }


    public void fetchWords(){
        DatabaseHandler dbh = new DatabaseHandler( mSolo.getCurrentActivity().getApplicationContext() );

        SQLiteDatabase db = dbh.getWritableDatabase();

        dbh.addWordWithTranslation(db,"Hello world","witaj świecie");
        Cursor cursor = dbh.getWordsStartingFrom( mSolo.getCurrentActivity().getApplicationContext(), 'w' ,"Hello world" );
        assertEquals(1,cursor.getColumnCount());
        assertEquals("Hello world",  cursor.getString(cursor.getColumnIndex(Constants.Dictionary.WORD.getTitle())));


    }
}