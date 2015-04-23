package com.example.gonzalo.tabhostswipe;

import android.content.Context;
import android.view.View;
import android.widget.TabHost;

/**
 * Created by Gonzalo on 23/04/2015.
 */
public class TabFactory implements TabHost.TabContentFactory {

    private final Context mContext;

    /**
     * @param context
     */
    public TabFactory(Context context) {
        mContext = context;
    }

    /** (non-Javadoc)
     * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
     */
    public View createTabContent(String tag) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
    }
}
