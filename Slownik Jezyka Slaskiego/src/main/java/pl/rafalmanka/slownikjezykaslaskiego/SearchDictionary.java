package pl.rafalmanka.slownikjezykaslaskiego;

import pl.rafalmanka.slownikjezykaslaskiego.*;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;



public class SearchDictionary extends ListActivity {


    private static final String TAG = SearchDictionary.class
            .getSimpleName();
    private TextView mTopBarEditText;
    private boolean isSilesianToPolish=true;

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

        mTopBarEditText = (TextView) findViewById(R.id.topbar_editText);
        mTopBarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2,int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                populateListView(editable.toString());
            }
        });
        populateListView(null);

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
                populateListView(null);
                mTopBarEditText.setText("");
            }
        });

        FlavourUtils.getAds(this);
    }

    private void populateListView(String regex) {

        String query;
        if (null == regex || regex.equals(""))
            query = "%";
        else
            query = regex;

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

                blogPost.put( Constants.Dictionary.WORD.getTitle(), cursor.getString(cursor.getColumnIndex(translateFrom)) );
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

        WordListAdapter adapter = new WordListAdapter(this, blogPosts);
        setListAdapter(adapter);

        cursor.close();
    }



}
