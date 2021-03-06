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
import uk.co.syski.client.android.model.database.entity.data.StorageDataEntity;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.viewmodel.SystemStorageDataViewModel;

public class VariableStorageTransfersGraph extends SyskiActivity {

    GraphView graph;
    LineGraphSeries<DataPoint> loadSeries;
    private int mLastXValue = 0;
    private float currentMaxY = 100;

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
        graph.getViewport().setMaxY(100);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(3);
        graph.getGridLabelRenderer().setHumanRounding(true);

        graph.getGridLabelRenderer().setVerticalAxisTitle("Storage Transfers");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time (s)");

        SystemStorageDataViewModel viewModel = ViewModelProviders.of(this).get(SystemStorageDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<StorageDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<StorageDataEntity> storageEntities) {
                if (storageEntities.size() > 0)
                {
                    for (int i = 0; i < storageEntities.size(); i++) {
                        StorageDataEntity current = storageEntities.get(i);
                        if(current.Time > currentMaxY) {
                            currentMaxY = current.Time;
                        }
                        loadSeries.appendData(new DataPoint(mLastXValue, current.Transfers), true, (mLastXValue / 3 + 1));
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
