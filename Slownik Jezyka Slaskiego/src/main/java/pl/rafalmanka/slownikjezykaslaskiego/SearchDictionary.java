package pl.rafalmanka.slownikjezykaslaskiego;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;



public class SearchDictionary extends ListActivity {


    private static final String TAG = SearchDictionary.class
            .getSimpleName();
    private TextView mTopBarEditText;
    private boolean isSilesianToPolish=true;
    private ArrayList<HashMap<String, String>> mDictionaryList;
    private ProgressBar mProgressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //menu.add( getResources().getString(R.string.suggest) );
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

        mProgressBar = (ProgressBar) findViewById(R.id.dictionary_progressBar);

        mTopBarEditText = (TextView) findViewById(R.id.topbar_editText);
        mTopBarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2,int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.bringToFront();
                new PopulateListView().execute(editable.toString());
            }
        });
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.bringToFront();
        new PopulateListView().execute(null);

        LinearLayout ll = (LinearLayout) findViewById(R.id.titlebar_linearlayout_tap_to_change_directions);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView iv = (ImageView) findViewById(R.id.titlebar_arrow_tap_to_change_directions);
                TextView silesianTextView = (TextView) findViewById(R.id.titlebar_silesian_textView);
                TextView polishTextView = (TextView) findViewById(R.id.titlebar_polish_textView);
                if(isSilesianToPolish){
                    isSilesianToPolish=false;
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.left_arrow));
                    silesianTextView.setText(R.string.to_silesian);
                    polishTextView.setText(R.string.from_polish);
                }else{
                    isSilesianToPolish=true;
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow));
                    silesianTextView.setText(R.string.from_silesian);
                    polishTextView.setText(R.string.to_polish);
                }
                new PopulateListView().execute(null);
                mTopBarEditText.setText("");
            }
        });

        FlavourUtils.getAds(this);
    }


    private class PopulateListView extends AsyncTask<String,Void,ArrayList<HashMap<String, String>>>{

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> blogPosts) {
            mDictionaryList = blogPosts;
            handlePopulateDictionary();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... regex) {
            String query;
            if (null == regex[0] || regex[0].equals(""))
                query = "%";
            else
                query = regex[0];

            DatabaseHandler dbh = new DatabaseHandler(getApplicationContext());

            String translateFrom="";
            String translateTo="";
            Cursor cursor;
            if(isSilesianToPolish){
                translateFrom=Constants.Dictionary.WORD.getTitle();
                translateTo=Constants.Dictionary.TRANSLATION.getTitle();
                cursor = dbh
                        .getWordsStartingFrom(getApplicationContext(), 'w',  query);
            } else {
                translateTo =Constants.Dictionary.WORD.getTitle();
                translateFrom =Constants.Dictionary.TRANSLATION.getTitle();
                cursor = dbh
                        .getWordsStartingFrom(getApplicationContext(), 't', query);
            }

            ArrayList<HashMap<String, String>> blogPosts = new ArrayList<HashMap<String, String>>();


            if (cursor.getCount() > 0) {
                do {
                    HashMap<String, String> blogPost = new HashMap<String, String>();

                    blogPost.put(Constants.Dictionary.WORD.getTitle(), cursor.getString(cursor.getColumnIndex(translateFrom)));
                    blogPost.put( Constants.Dictionary.TRANSLATION.getTitle(), cursor.getString(cursor.getColumnIndex(translateTo)) );


                    blogPosts.add(blogPost);
                } while (cursor.moveToNext());
            } else {

                HashMap<String, String> blogPost = new HashMap<String, String>();
                blogPost.put(Constants.Dictionary.WORD.getTitle(),
                        getString(R.string.no_words_to_display));
                blogPost.put(Constants.Dictionary.TRANSLATION.getTitle(), "");
                blogPosts.add(blogPost);
            }

           cursor.close();
           return  blogPosts;
        }
    }

    private void handlePopulateDictionary() {
        mProgressBar.setVisibility(View.INVISIBLE);
        WordListAdapter adapter = new WordListAdapter(this, mDictionaryList);
        setListAdapter(adapter);
    }


}
