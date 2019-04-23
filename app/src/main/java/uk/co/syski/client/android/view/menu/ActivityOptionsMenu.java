package uk.co.syski.client.android.view.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public interface ActivityOptionsMenu {
    public boolean create(Menu menu, MenuInflater inflater);

    public boolean onItemSelected(MenuItem item, Activity context);
}
