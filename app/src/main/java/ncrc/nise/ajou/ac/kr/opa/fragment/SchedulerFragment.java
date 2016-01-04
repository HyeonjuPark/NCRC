package ncrc.nise.ajou.ac.kr.opa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import ncrc.nise.ajou.ac.kr.opa.R;
import ncrc.nise.ajou.ac.kr.opa.activity.MainActivity;
import ncrc.nise.ajou.ac.kr.opa.scheduler.SchedulerDailyActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class SchedulerFragment extends Fragment {
    private CalendarView calendarView;


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SchedulerFragment newInstance(int sectionNumber) {
        SchedulerFragment fragment = new SchedulerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SchedulerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_scheduler, container, false);

        calendarView = (CalendarView)rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String currDate = "" + year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth);

                Intent intent = new Intent(rootView.getContext(), SchedulerDailyActivity.class);
                Log.i("currDate", currDate);
                intent.putExtra("currDate", currDate);
                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

