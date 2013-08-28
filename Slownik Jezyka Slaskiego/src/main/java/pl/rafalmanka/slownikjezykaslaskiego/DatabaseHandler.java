package pl.rafalmanka.slownikjezykaslaskiego;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rafal on 8/15/13.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    private Context mContext;

    public DatabaseHandler(Context context) {
        super(context, Constants.Database.DATABASE.getTitle(), null, Constants.Database.DATABASE.getVersion());
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE "
                + Constants.Database.TABLE_DICTIONARY.getTitle() + "("
                + Constants.Database.COLUMN_DICTIONARY_ID.getTitle() + " INTEGER PRIMARY KEY, "
                + Constants.Database.COLUMN_DICTIONARY_TITLE.getTitle() + " TEXT, "
                + Constants.Database.COLUMN_DICTIONARY_TRANSLATION.getTitle() + " TEXT) ";

        sqLiteDatabase.execSQL(createTable);
        populateLanguageTableFromFileManager(sqLiteDatabase);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int version_1, int version_2) {
       String dropQuery = "DROP TABLE IF EXISTS "+Constants.Database.TABLE_DICTIONARY.getTitle();
        sqLiteDatabase.execSQL(dropQuery);
        onCreate(sqLiteDatabase);
    }

    public synchronized Cursor getWordsStartingFrom(Context context, String word) {
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor = db.query(Constants.Database.TABLE_DICTIONARY.getTitle(), 
                null,
                		Constants.Database.COLUMN_DICTIONARY_TITLE.getTitle() + " LIKE '" + word + "%' " +
                "OR "+	Constants.Database.COLUMN_DICTIONARY_TITLE.getTitle() + " LIKE '% " + word + "%' ",
                null, null, null, null, null);
        
        cursor.moveToFirst(); 
        db.close(); 
        return cursor;
    }

    public synchronized Long addWordWithTranslation(SQLiteDatabase db, String word, String translation) { 
        ContentValues values = new ContentValues();
        values.put(Constants.Database.COLUMN_DICTIONARY_TITLE.getTitle(), word);
        values.put(Constants.Database.COLUMN_DICTIONARY_TRANSLATION.getTitle(), translation);
        
        if(!db.isOpen())
        	db = getWritableDatabase();
        
        Long lastInsertedId = db.insert(Constants.Database.TABLE_DICTIONARY.getTitle(), null, values);
        values.clear();
        return lastInsertedId;
    }

    private void populateLanguageTableFromFileManager(SQLiteDatabase db) {

        List<String[]> list = getfromCSV();

        for(String[] langInfo : list){
            addWordWithTranslation(db, langInfo[0], langInfo[1]);
        }

    }

    public List<String[]> getfromCSV() {

        List<String[]> list = new ArrayList<String[]>();

        try {
            InputStream mInputStream = mContext.getAssets().open("dictionary.csv");

            Reader reader = new InputStreamReader(mInputStream);
            BufferedReader buffreader = new BufferedReader(reader);

            String line = "";
            while ((line = buffreader.readLine()) != null) {
                String[] phrase = line.split(";");
                if (phrase.length == 2) {
                    String[] row = new String[2];
                    row[0] = phrase[0];
                    row[1] = phrase[1];
                    list.add(row);
                } else {
                    Log.d(TAG,
                            "CSV construction wrong, ommitting: "
                                    + line);
                }
            }
            buffreader.close();
            mInputStream.close();
            mInputStream = null;
            return list;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "unable to open file", e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "unable to read file", e);
            return null;
        }
    }

}
