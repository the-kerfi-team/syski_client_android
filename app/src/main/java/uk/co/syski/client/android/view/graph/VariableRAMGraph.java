package uk.co.syski.client.android.view.graph;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.entity.data.CPUDataEntity;
import uk.co.syski.client.android.data.entity.data.RAMDataEntity;
import uk.co.syski.client.android.view.RAMActivity;
import uk.co.syski.client.android.viewmodel.SystemRAMDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemRAMViewModel;

public class VariableRAMGraph extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> loadSeries;
    private int mLastXValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_ramgraph);
        graph = findViewById(R.id.ramGraph);

        loadSeries = new LineGraphSeries<DataPoint>();
        graph.addSeries(loadSeries);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(3);
        graph.getGridLabelRenderer().setHumanRounding(true);

        graph.getGridLabelRenderer().setVerticalAxisTitle("Free RAM (MB)");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time (s)");

        SystemRAMViewModel staticViewModel = ViewModelProviders.of(this).get(SystemRAMViewModel.class);
        staticViewModel.get().observe(this, new Observer<List<RAMEntity>>() {
            @Override
            public void onChanged(@Nullable List<RAMEntity> ramEntities) {
                if (ramEntities.size() > 0)
                {
                    long totalRAM = 0;
                    for (int i = 0; i < ramEntities.size(); i++) {
                        totalRAM += ramEntities.get(i).MemoryBytes;
                    }

                    graph.getViewport().setMaxY(totalRAM / (1024 * 1024));
                }
            }
        });

        SystemRAMDataViewModel viewModel = ViewModelProviders.of(this).get(SystemRAMDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<RAMDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<RAMDataEntity> ramEntities) {
                if (ramEntities.size() > 0)
                {
                    for (int i = 0; i < ramEntities.size(); i++) {
                        RAMDataEntity current = ramEntities.get(i);
                        loadSeries.appendData(new DataPoint(mLastXValue, current.Free), true, (mLastXValue / 3 + 1));
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
