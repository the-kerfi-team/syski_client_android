package uk.co.syski.client.android.view.graph;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.data.CPUDataEntity;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.viewmodel.SystemCPUDataViewModel;

public class VariableCPUProcessesGraph extends SyskiActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> loadSeries;
    private int mLastXValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        optionsMenu = new SyskiOptionsMenu();

        graph = findViewById(R.id.graph);

        loadSeries = new LineGraphSeries<DataPoint>();
        graph.addSeries(loadSeries);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(300);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(3);
        graph.getGridLabelRenderer().setHumanRounding(true);

        graph.getGridLabelRenderer().setVerticalAxisTitle("CPU Processes");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time (s)");

        SystemCPUDataViewModel viewModel = ViewModelProviders.of(this).get(SystemCPUDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<CPUDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<CPUDataEntity> cpuEntities) {
                if (cpuEntities.size() > 0)
                {
                    for (int i = 0; i < cpuEntities.size(); i++) {
                        CPUDataEntity current = cpuEntities.get(i);
                        loadSeries.appendData(new DataPoint(mLastXValue, current.Processes), true, (mLastXValue / 3 + 1));
                        mLastXValue += 3;
                    }

                    graph.getViewport().setMinX(0);
                    graph.getViewport().setMaxX(mLastXValue - 3);

                    graph.getGridLabelRenderer().setNumHorizontalLabels(6);
                }
            }
        });
    }

}
