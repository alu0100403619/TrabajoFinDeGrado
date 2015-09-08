//...
public class StudentTabActivity extends TabActivity {
        //...
        //Annadiendo las Tabs
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        //Student Tab
        intent = new Intent().setClass(this, StudentActivity.class);
        //...
        spec = tabHost.newTabSpec(getString(R.string.tab_alumnos))
                .setIndicator(getString(R.string.tab_alumnos)).setContent(intent);
        tabHost.addTab(spec);
        //Otras tabs
        tabHost.setCurrentTab(0);
    }
//...
}
