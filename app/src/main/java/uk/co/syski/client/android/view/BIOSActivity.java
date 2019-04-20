package uk.co.syski.client.android.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.adapter.listview.HeadedValueListAdapter;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.view.model.DoubleHeadedValueModel;
import uk.co.syski.client.android.view.model.HeadedValueModel;

public class BIOSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_overview);

        DoubleHeadedValueFragment topFragment = DoubleHeadedValueFragment.newInstance(
            new DoubleHeadedValueModel(
                R.drawable.placeholder,
                "Caption",
                "BIOS Date: 12/27/17 20:48:57 Ver: 05.0000C",
                "Manufacturer",
                "American Megatrends Inc."
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.topFragment, topFragment).commit();

        ArrayList<HeadedValueModel> biosData = new ArrayList<>();

        biosData.add(new HeadedValueModel(R.drawable.ic_version, "Version", "P3.40"));
        biosData.add(new HeadedValueModel(R.drawable.placeholder, "Date", "20171227000000.000000+000"));

        ListView dataList = findViewById(R.id.listView);
        dataList.setAdapter(new HeadedValueListAdapter(this, biosData));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_syslist) {
            Intent settings = new Intent(this, SystemListMenu.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
