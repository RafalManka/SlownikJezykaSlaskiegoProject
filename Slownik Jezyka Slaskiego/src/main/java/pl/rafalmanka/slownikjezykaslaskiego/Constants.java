package pl.rafalmanka.slownikjezykaslaskiego;

/**
 * Created by rafal on 8/15/13.
 */
public class Constants {

    public enum Database {
        DATABASE("SlownikSlaski",11),
        TABLE_DICTIONARY("Dictionary"),
        COLUMN_DICTIONARY_ID("id_dictionary"),
        COLUMN_DICTIONARY_TITLE("title_dictionary"),
        COLUMN_DICTIONARY_TRANSLATION("translation_dictionary" );

        String title;
        int version;

        private Database(String hasTitle,int hasVersion){
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

		private Dictionary(String title){
			this.title=title;
		}
		
		public String getTitle(){
            return title;
        }
	}
	
	public enum Menu {
		SUGGEST("suggest");
		
		private String title;

		private Menu(String title){
			this.title=title;
		}
		
		public String getTitle(){
            return title;
        }
	}



}
