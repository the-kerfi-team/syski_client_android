package uk.co.syski.client.android.view.activity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;

public class ProcessListActivity extends SyskiActivity {

    ExpandableListView processList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_list);

        optionsMenu = new SyskiOptionsMenu();

        processList = findViewById(R.id.processList);
    }
}
