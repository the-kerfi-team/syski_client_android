package uk.co.syski.client.android.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import uk.co.syski.client.android.view.menu.ActivityOptionsMenu;

public abstract class SyskiActivity extends AppCompatActivity {

    protected ActivityOptionsMenu optionsMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return optionsMenu.create(menu, getMenuInflater());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return optionsMenu.onItemSelected(item, this);
    }

}
