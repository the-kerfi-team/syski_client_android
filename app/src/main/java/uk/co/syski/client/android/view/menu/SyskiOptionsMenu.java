package uk.co.syski.client.android.view.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.SettingsActivity;
import uk.co.syski.client.android.view.SystemListMenu;

public class SyskiOptionsMenu implements ActivityOptionsMenu {

    @Override
    public boolean create(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onItemSelected(MenuItem item, Activity context) {
        int id = item.getItemId();

        Intent settings = null;
        switch (id) {
            case R.id.action_syslist:
                settings = new Intent(context, SystemListMenu.class);
                break;
            case R.id.action_settings:
                settings = new Intent(context, SettingsActivity.class);
                break;
        }

        if (settings != null)
            context.startActivity(settings);

        return settings != null;
    }
}
