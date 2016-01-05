package ncrc.nise.ajou.ac.kr.opa.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ncrc.nise.ajou.ac.kr.opa.R;
import ncrc.nise.ajou.ac.kr.opa.data.ItemData;
import ncrc.nise.ajou.ac.kr.opa.data.MyAdapter;


public class RecommendedExerListFragment extends Fragment {

    private ArrayAdapter<String> mExerciseAdapter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public RecommendedExerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.exer_list, container, false);

        // Create some dummy data for the ListView.
        String[] exerciseArray = {
                "팔 굽혀 펴기",
                "자전거",
                "스키",
                "탁구"
        };

        ItemData itemsData[] = { new ItemData("자전거 타기",R.drawable.bicyclist_resize),
                new ItemData("팔굽혀 펴기",R.drawable.pushups_resize),
                new ItemData("스키",R.drawable.skiing_resize),
                new ItemData("탁구",R.drawable.pingpong_resize)};

       /* List<String> weekForecast = new ArrayList<String>(Arrays.asList(exerciseArray));

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mExerciseAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_exercise, // The name of the layout ID
                        R.id.list_item_exercise_textview, // The Id of the textview to populate
                        weekForecast);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_recExer_list);
        listView.setAdapter(mExerciseAdapter);*/

        //
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(itemsData);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

}
