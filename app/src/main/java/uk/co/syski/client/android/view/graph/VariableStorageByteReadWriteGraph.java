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

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.data.StorageDataEntity;
import uk.co.syski.client.android.viewmodel.SystemStorageDataViewModel;

public class VariableStorageByteReadWriteGraph extends AppCompatActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> readSeries;
    LineGraphSeries<DataPoint> writeSeries;
    private int mLastXValue = 0;
    private float currentMaxY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_cpugraph);
        graph = findViewById(R.id.graph);

        readSeries = new LineGraphSeries<DataPoint>();
        graph.addSeries(readSeries);

        writeSeries = new LineGraphSeries<DataPoint>();
        graph.addSeries(writeSeries);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(3);
        graph.getGridLabelRenderer().setHumanRounding(true);

        graph.getGridLabelRenderer().setVerticalAxisTitle("Storage Byte Reads and Writes");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time (s)");

        SystemStorageDataViewModel viewModel = ViewModelProviders.of(this).get(SystemStorageDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<StorageDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<StorageDataEntity> storageEntities) {
                if (storageEntities.size() > 0)
                {
                    for (int i = 0; i < storageEntities.size(); i++) {
                        StorageDataEntity current = storageEntities.get(i);
                        if (current.ByteReads > currentMaxY) {
                            currentMaxY = current.ByteReads;
                        } else if (current.ByteWrites > currentMaxY) {
                            currentMaxY = current.ByteWrites;
                        }
                        readSeries.appendData(new DataPoint(mLastXValue, current.ByteReads), true, (mLastXValue / 3 + 1));
                        writeSeries.appendData(new DataPoint(mLastXValue, current.ByteWrites), true, (mLastXValue / 3 + 1));
                        mLastXValue += 3;
                    }

                    graph.getViewport().setMinX(0);
                    graph.getViewport().setMaxX(mLastXValue - 3);

                    graph.getViewport().setMaxY(currentMaxY);

                    graph.getGridLabelRenderer().setNumHorizontalLabels(6);
                }
            }
        });

    }
}
