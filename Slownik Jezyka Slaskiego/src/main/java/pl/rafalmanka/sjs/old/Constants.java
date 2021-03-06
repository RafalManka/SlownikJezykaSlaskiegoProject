package pl.rafalmanka.sjs.old;

/**
 * Created by rafal on 8/15/13.
 */
public class Constants {

    public enum Database {
        DATABASE("SlownikSlaski",19),

        TABLE_WORD("table_word"),
        COLUMN_WORD_ID("word_id"),
        COLUMN_WORD("word"),

        TABLE_WORD_HAS_TRANSLATION("word_has_translation"),
        COLUMN_TRANSLATION_ID("translation_id"),
        COLUMN_WORD_HAS_TRANSLATION_ID("word_has_translation_id"),

        ;

        String title;
        int version;

        Database(String hasTitle, int hasVersion){
            title = hasTitle;
            version = hasVersion;
        }

        Database(String hasTitle) {
            title = hasTitle;
        }

        public String getTitle(){
            return title;
        }

        public int getVersion(){
            return version;
        }
    }

	public enum Dictionary {
		WORD("word"),
		TRANSLATION("translation");
		
		private String title;

		Dictionary(String title){
			this.title=title;
		}
		
		public String getTitle(){
            return title;
        }
	}


}
