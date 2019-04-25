package uk.co.syski.client.android.view.graph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;

public class VariableNetworkGraph extends SyskiActivity {

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        optionsMenu = new SyskiOptionsMenu();

        graph = findViewById(R.id.graph);
    }
}
