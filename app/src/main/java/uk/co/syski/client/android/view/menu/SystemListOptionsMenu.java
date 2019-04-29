package uk.co.syski.client.android.view.menu;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.activity.SettingsActivity;

public class SystemListOptionsMenu implements ActivityOptionsMenu {
    @Override
    public boolean create(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sys_list_menu, menu);
        return true;
    }

    @Override
    public boolean onItemSelected(MenuItem item, Activity context) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(context, SettingsActivity.class);
            context.startActivity(settings);
            return true;
        }

        return false;
    }
}
