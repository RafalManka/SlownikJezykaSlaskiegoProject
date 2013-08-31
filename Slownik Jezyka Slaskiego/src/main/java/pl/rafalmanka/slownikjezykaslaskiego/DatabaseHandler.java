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
        Log.d(TAG, "creating table word");
        String CREATE_TABLE_WORD = "CREATE TABLE IF NOT EXISTS "
                + Constants.Database.TABLE_WORD.getTitle() + " ("
                + Constants.Database.COLUMN_WORD_ID.getTitle() + " INTEGER PRIMARY KEY, "
                + Constants.Database.COLUMN_WORD.getTitle() + " TEXT) ";
        Log.d(TAG, "Executing: " + CREATE_TABLE_WORD);
        sqLiteDatabase.execSQL(CREATE_TABLE_WORD);

        Log.d(TAG, "creating table word_has_translation");
        String CREATE_TABLE_WORD_HAS_TRANSLATION = "CREATE TABLE IF NOT EXISTS "
                + Constants.Database.TABLE_WORD_HAS_TRANSLATION.getTitle() + "  ("
                + Constants.Database.COLUMN_WORD_HAS_TRANSLATION_ID.getTitle() + " INTEGER PRIMARY KEY, "
                + Constants.Database.COLUMN_WORD_ID.getTitle() + " INTEGER REFERENCES "
                + Constants.Database.TABLE_WORD.getTitle() + " (" + Constants.Database.COLUMN_WORD_ID.getTitle() + "), "
                + Constants.Database.COLUMN_TRANSLATION_ID.getTitle() + " INTEGER REFERENCES "
                + Constants.Database.TABLE_WORD.getTitle() + " (" + Constants.Database.COLUMN_WORD_ID.getTitle() + ")   ) ";
        Log.d(TAG, "Executing: " + CREATE_TABLE_WORD_HAS_TRANSLATION);
        sqLiteDatabase.execSQL(CREATE_TABLE_WORD_HAS_TRANSLATION);

        populateLanguageTableFromFileManager(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int version_1, int version_2) {
        String dropQuery = "DROP TABLE IF EXISTS " + Constants.Database.TABLE_WORD.getTitle();
        sqLiteDatabase.execSQL(dropQuery);
        dropQuery = "DROP TABLE IF EXISTS " + Constants.Database.TABLE_WORD_HAS_TRANSLATION.getTitle();
        sqLiteDatabase.execSQL(dropQuery);
        onCreate(sqLiteDatabase);
    }

    public synchronized Cursor getWordsStartingFrom(Context context, char direction, String word) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT *," +
                " w."+Constants.Database.COLUMN_WORD.getTitle()+" AS "+Constants.Dictionary.WORD.getTitle()+","+
                " t."+Constants.Database.COLUMN_WORD.getTitle()+" AS "+Constants.Dictionary.TRANSLATION.getTitle()+
                " FROM " + Constants.Database.TABLE_WORD.getTitle() + " AS w " +
                " INNER JOIN " + Constants.Database.TABLE_WORD_HAS_TRANSLATION.getTitle() +
                " AS wht ON wht." + Constants.Database.COLUMN_WORD_ID.getTitle() + "=w." + Constants.Database.COLUMN_WORD_ID.getTitle() + "" +
                " INNER JOIN " + Constants.Database.TABLE_WORD.getTitle() +
                " AS t ON wht." + Constants.Database.COLUMN_TRANSLATION_ID.getTitle() + "=t." + Constants.Database.COLUMN_WORD_ID.getTitle() + "" +
                " WHERE "+direction+"." + Constants.Database.COLUMN_WORD.getTitle() + " LIKE '" + word +"%'"+
                " OR "+direction+"." + Constants.Database.COLUMN_WORD.getTitle() + " LIKE ' " + word + "%'"+
                " ORDER BY "+direction+"." + Constants.Database.COLUMN_WORD.getTitle()+" ASC"
                ;

        Log.d(TAG,"query: "+query);
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        db.close();
        return cursor;
    }

    public synchronized Long addWordWithTranslation(SQLiteDatabase db, String word, String translation) {

        long lastInserterWordId = getLastInsertedWordId(db, word, Constants.Database.TABLE_WORD.getTitle(), Constants.Database.COLUMN_WORD.getTitle());
        long lastInserterTranslationId = getLastInsertedWordId(db, translation, Constants.Database.TABLE_WORD.getTitle(), Constants.Database.COLUMN_WORD.getTitle());
        setWordTranslationRelation(db,lastInserterWordId,lastInserterTranslationId);

        return lastInserterWordId;
    }

    private synchronized void setWordTranslationRelation(SQLiteDatabase db, long lastInserterWordId, long lastInserterTranslationId) {
        if (!db.isOpen())
            db = getWritableDatabase();

        String query="SELECT * FROM "+Constants.Database.TABLE_WORD_HAS_TRANSLATION.getTitle()+" WHERE "+
                Constants.Database.COLUMN_WORD_ID.getTitle()+"='"+lastInserterWordId+"' AND "+
                Constants.Database.COLUMN_TRANSLATION_ID.getTitle()+"='"+lastInserterTranslationId+"'";
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.getCount()==0){
            ContentValues values = new ContentValues();
            values.put(Constants.Database.COLUMN_WORD_ID.getTitle(),lastInserterWordId);
            values.put(Constants.Database.COLUMN_TRANSLATION_ID.getTitle(),lastInserterTranslationId);

            db.insert(Constants.Database.TABLE_WORD_HAS_TRANSLATION.getTitle(), null, values);
            values.clear();
        }
        cursor.close();

    }

    private synchronized long getLastInsertedWordId(SQLiteDatabase db, String word, String tableName , String columnName) {
        if (!db.isOpen())
            db = getWritableDatabase();

        String query = "SELECT * FROM "+tableName+" WHERE "+columnName+"='"+word+"'";
        Cursor cursor = db.rawQuery(query,null);
        long lastInsertedId = -1;
        if(cursor.getCount()==0){
            ContentValues values = new ContentValues();
            values.put(columnName,word);
            lastInsertedId = db.insert(tableName, null, values);
            values.clear();
        } else {
            cursor.moveToFirst();
            lastInsertedId = cursor.getLong(cursor.getColumnIndex(Constants.Database.COLUMN_WORD.getTitle()));
        }
        cursor.close();
        return lastInsertedId;
    }

    private void populateLanguageTableFromFileManager(SQLiteDatabase db) {

        List<String[]> list = getfromCSV();

        for (String[] langInfo : list) {
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
