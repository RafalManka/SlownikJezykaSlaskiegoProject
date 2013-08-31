package pl.rafalmanka.slownikjezykaslaskiego;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class WordListAdapter extends BaseAdapter {

    private static final String TAG = WordListAdapter.class.getSimpleName();
    private static LayoutInflater mInflater = null;
    private ArrayList<HashMap<String, String>> mData;

    public WordListAdapter(Activity activity,
                           ArrayList<HashMap<String, String>> data) {
        mData = data;
        mInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = mInflater.inflate(R.layout.adapter_list_item, null);

        TextView name = (TextView) vi
                .findViewById(R.id.adapter_list_item_textView_word);

        name.setTextSize(16);

        HashMap<String, String> item = new HashMap<String, String>();
        item = mData.get(position);

        name.setText(item.get(Constants.Dictionary.WORD.getTitle()));

        final String translation = item.get(Constants.Dictionary.TRANSLATION
                .getTitle());

        if (!translation.equals("")) {

            vi.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    LinearLayout linearLayout = (LinearLayout) view
                            .findViewById(666);

                    if (null == linearLayout) {
                        linearLayout = new LinearLayout(view.getContext());
                        linearLayout.setId(666);
                        linearLayout.setPadding(0, 10, 0, 0);
                        linearLayout.setLayoutParams(new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.MATCH_PARENT));

                        ImageView imageView = new ImageView(view.getContext());
                        imageView.setLayoutParams(new LayoutParams(
                                LayoutParams.WRAP_CONTENT, 25));
                        imageView.setImageResource(R.drawable.arrow);

                        linearLayout.addView(imageView);

                        TextView textView = new TextView(
                                view.getContext());

                        textView.setText(translation);
                        textView.setLayoutParams(new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT));
                        textView.setPadding(10, 10, 0, 0);
                        textView.setTextSize(16);
                        textView.setTextColor(view.getResources().getColor(
                                R.color.darkergrey_titlebar));
                        textView.setBackgroundColor(view.getResources()
                                .getColor(android.R.color.transparent));

                        linearLayout.addView(textView);

                        ((ViewGroup) view).addView(linearLayout);
                    } else {
                        if (!linearLayout.isShown())
                            linearLayout.setVisibility(View.VISIBLE);
                        else
                            linearLayout.setVisibility(View.GONE);

                    }

                }
            });
        }

        return vi;
    }

}
