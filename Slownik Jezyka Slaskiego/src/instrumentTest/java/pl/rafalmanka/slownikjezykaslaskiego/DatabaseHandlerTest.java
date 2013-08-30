package pl.rafalmanka.slownikjezykaslaskiego;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Created by rafal on 8/23/13.
 */
@RunWith(RobolectricTestRunner.class)
public class DatabaseHandlerTest {

    public static final String TAG = DatabaseHandlerTest.class.getSimpleName();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void fetchWords(){
        DatabaseHandler dbh = new DatabaseHandler(Robolectric.getShadowApplication().getApplicationContext() );

        SQLiteDatabase db = dbh.getWritableDatabase();

        dbh.addWordWithTranslation(db,"Hello world","witaj świecie");

        assertThat("Hello world", equalTo("Hello world"));
        //assertEquals("Hello world", "witaj świecie");
    }
}