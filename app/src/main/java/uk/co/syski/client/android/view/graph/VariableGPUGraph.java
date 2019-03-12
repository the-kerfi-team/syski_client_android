package uk.co.syski.client.android.view.graph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;

import uk.co.syski.client.android.R;

public class VariableGPUGraph extends AppCompatActivity {

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_gpugraph);

        graph = findViewById(R.id.graph);
    }
}
