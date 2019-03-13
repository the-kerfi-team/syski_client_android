package uk.co.syski.client.android.view.graph;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.Random;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.data.CPUDataEntity;
import uk.co.syski.client.android.data.repository.SystemCPUDataRepository;
import uk.co.syski.client.android.viewmodel.SystemCPULoadDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemCPUViewModel;

public class VariableCPUGraph extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> loadSeries;
    private int graph2LastXValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_cpugraph);
        graph = findViewById(R.id.graph);

        loadSeries = new LineGraphSeries<DataPoint>(generateData());
        graph.addSeries(loadSeries);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);

        SystemCPULoadDataViewModel viewModel = ViewModelProviders.of(this).get(SystemCPULoadDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<CPUDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<CPUDataEntity> cpuEntities) {
                if (cpuEntities.size() > 0)
                {
                    for (int i = 0; i < cpuEntities.size(); i++) {
                        graph2LastXValue += 10;
                        CPUDataEntity current = cpuEntities.get(i);
                        loadSeries.appendData(new DataPoint(current.Load, graph2LastXValue), true, 40);
                    }
                }
            }
        });

    }

    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = mRand.nextDouble()*0.15+0.3;
            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }

}
