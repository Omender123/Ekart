package wrteam.ecommerce.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import wrteam.ecommerce.app.R;
import wrteam.ecommerce.app.activity.OrderListActivity;
import wrteam.ecommerce.app.adapter.TrackerAdapter;
import wrteam.ecommerce.app.helper.Session;
import wrteam.ecommerce.app.model.OrderTracker;

import java.util.ArrayList;


public class OrderTrackerListFragment extends Fragment {

    RecyclerView recyclerView;
    TextView nodata;
    ProgressBar progressbar;
    Session session;
    private ArrayList<OrderTracker> orderTrackerArrayList;
    //QuizListAdapter adapter;
    String quiztype;
    int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ordertrackerlist, container, false);

        pos = getArguments().getInt("pos");

        session = new Session(getActivity());
        progressbar = (ProgressBar) v.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleview);
        nodata = (TextView) v.findViewById(R.id.nodata);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (pos == 0)
            orderTrackerArrayList = OrderListActivity.orderTrackerslist;
        else if (pos == 1)
            orderTrackerArrayList = OrderListActivity.processedlist;
        else if (pos == 2)
            orderTrackerArrayList = OrderListActivity.shippedlist;
        else if (pos == 3)
            orderTrackerArrayList = OrderListActivity.deliveredlist;
        else if (pos == 4)
            orderTrackerArrayList = OrderListActivity.cancelledlist;

        if (orderTrackerArrayList.size() == 0)
            nodata.setVisibility(View.VISIBLE);

        recyclerView.setAdapter(new TrackerAdapter(getActivity(), orderTrackerArrayList));
progressbar.setVisibility(View.GONE);
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

    }
}
