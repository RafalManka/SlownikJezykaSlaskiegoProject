package pl.rafalmanka.slownikjezykaslaskiego;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import pl.rafalmanka.slownikjezykaslaskiego.R;


public class SearchDictionary extends ListActivity implements OnClickListener{

    private static final String TAG = SearchDictionary.class
            .getSimpleName();
    private TextView mTopBarEditText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add( getResources().getString(R.string.suggest) );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == 0){
            Intent intent = new Intent(this, SuggestWordActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.setLayout(this, R.layout.activity_starting_point, R.layout.title_bar);

        mTopBarEditText = (TextView) findViewById(R.id.topbar_editText);
        mTopBarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2,
                                      int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                populateListView(editable.toString());
            }
        });

        populateListView(null);

        FlavourUtils.getAds(this);


    }

    private void populateListView(String regex) {

        String query;
        if (null == regex || regex.equals(""))
            query = "%";
        else
            query = regex;

        DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());
        Cursor cursor = dbh
                .getWordsStartingFrom(getApplicationContext(), query);
        ArrayList<HashMap<String, String>> blogPosts = new ArrayList<HashMap<String, String>>();

        if (cursor.getCount() > 0) {
            do {
                HashMap<String, String> blogPost = new HashMap<String, String>();
                blogPost.put(
                        Constants.Dictionary.WORD.getTitle(),
                        cursor.getString(cursor
                                .getColumnIndex(Constants.Database.COLUMN_DICTIONARY_TITLE
                                        .getTitle())));
                blogPost.put(
                        Constants.Dictionary.TRANSLATION.getTitle(),
                        cursor.getString(cursor
                                .getColumnIndex(Constants.Database.COLUMN_DICTIONARY_TRANSLATION
                                        .getTitle())));
                blogPosts.add(blogPost);
            } while (cursor.moveToNext());
        } else {
            HashMap<String, String> blogPost = new HashMap<String, String>();
            blogPost.put(Constants.Dictionary.WORD.getTitle(),
                    getString(R.string.no_words_to_display));
            blogPost.put(Constants.Dictionary.TRANSLATION.getTitle(), "");
            blogPosts.add(blogPost);
        }

        WordListAdapter adapter = new WordListAdapter(this, blogPosts);
        setListAdapter(adapter);

        cursor.close();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }



}
