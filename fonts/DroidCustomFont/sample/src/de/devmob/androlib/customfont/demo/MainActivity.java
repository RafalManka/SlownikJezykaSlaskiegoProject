package de.devmob.androlib.customfont.demo;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

/**
 * Simple main activity of the demo application to show the usage of the
 * custom font ui elements.
 * 
 * @author Friederike Wild
 */
public class MainActivity extends Activity
{
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item)
    {
        switch(item.getItemId())
        {
            /*
            case R.id.menu_settings:
            {

                return true;
            }
            */
            case R.id.menu_info:
            {
                showDialog(R.id.dialog_info);
                return true;
            }
        }

        // Default handling
        return super.onOptionsItemSelected(item);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateDialog(int)
     */
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case R.id.dialog_info:
            {
                // Create the requested dialog
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle(R.string.menu_info)
                        .setNeutralButton(android.R.string.ok, null);

                // Add a custom text view to enable a clickable link
                LayoutInflater layoutInflater = LayoutInflater.from(this);        
                final View textEntryView = layoutInflater.inflate(R.layout.layout_text_dialog, null);
                TextView messageTextView = ((TextView) textEntryView.findViewById(R.id.textMessage));
                messageTextView.setText(Html.fromHtml(this.getResources().getString(R.string.text_info)));
                messageTextView.setMovementMethod(LinkMovementMethod.getInstance());

                dialogBuilder.setView(textEntryView);

                return dialogBuilder.create();
            }
        }

        return null;
    }

}
