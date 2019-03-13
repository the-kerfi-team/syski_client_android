package uk.co.syski.client.android.view.graph;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.data.CPUDataEntity;
import uk.co.syski.client.android.viewmodel.SystemCPULoadDataViewModel;

public class VariableCPUProccessesGraph extends AppCompatActivity {

    GraphView graph;
    BarGraphSeries<DataPoint> loadSeries;
    private int mLastXValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_cpugraph);
        graph = findViewById(R.id.graph);

        loadSeries = new BarGraphSeries<DataPoint>();
        loadSeries.setSpacing(10);

// draw values on top
        loadSeries.setDrawValuesOnTop(true);
        loadSeries.setValuesOnTopColor(Color.BLUE);

        graph.addSeries(loadSeries);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(300);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(15);
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);
        graph.getGridLabelRenderer().setHumanRounding(false);

        SystemCPULoadDataViewModel viewModel = ViewModelProviders.of(this).get(SystemCPULoadDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<CPUDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<CPUDataEntity> cpuEntities) {
                if (cpuEntities.size() > 0)
                {
                    for (int i = 0; i < cpuEntities.size(); i++) {
                        CPUDataEntity current = cpuEntities.get(i);
                        loadSeries.appendData(new DataPoint(mLastXValue, current.Processes), true, 10);
                        mLastXValue += 3;
                    }
                }
            }
        });

    }

}
