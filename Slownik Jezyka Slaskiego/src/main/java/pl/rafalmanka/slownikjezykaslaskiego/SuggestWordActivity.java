package pl.rafalmanka.slownikjezykaslaskiego;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import pl.rafalmanka.slownikjezykaslaskiego.R;


public class SuggestWordActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtils.setLayout(this, R.layout.activity_suggest_word,
				R.layout.title_bar_no_searchbar);
		Button button = (Button) findViewById(R.id.suggest_button_submit);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.suggest_button_submit) {

			EditText your_nickname_editText = (EditText) findViewById(R.id.your_nickname_editText);
			EditText word_original_editText = (EditText) findViewById(R.id.word_original_editText);
			EditText word_translation_editText = (EditText) findViewById(R.id.word_translation_editText);
			EditText example_use_editText = (EditText) findViewById(R.id.example_use_editText);
			
			String body="Słowo: " + word_original_editText.getText().toString() + "\n" + "Tłumaczenie: "
					+ word_translation_editText.getText().toString() + "\n"
					+ "Przykład uzycia: " + example_use_editText.getText().toString() + "\n"
					+ "Użytkownik: " + your_nickname_editText.getText().toString() + "\n";
			
			ActivityUtils.sendEmail( getApplicationContext() ,"rafal@rafalmanka.pl", "propozycja nowego słowa", body);
		}
	}



}
