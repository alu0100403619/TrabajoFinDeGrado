//...
public class ChangeLanguageActivity extends Activity {
    public void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().
                getResources().getDisplayMetrics());
    }
}