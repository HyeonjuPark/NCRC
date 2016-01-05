
package ncrc.nise.ajou.ac.kr.opa.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import ncrc.nise.ajou.ac.kr.opa.R;
import ncrc.nise.ajou.ac.kr.opa.activity.MainActivity;
import ncrc.nise.ajou.ac.kr.opa.recommender.RecommendedExercisesActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExerciseRecommenderFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button buttonResult;
    private TextView FinalCaloryValue;
    private String place = "inside";
    private String strength = "weak";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ExerciseRecommenderFragment newInstance(int sectionNumber) {
        ExerciseRecommenderFragment fragment = new ExerciseRecommenderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ExerciseRecommenderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_exercise_recommender, container, false);
        FinalCaloryValue = (TextView)rootView.findViewById(R.id.textViewFinalCaloryValue);
        FinalCaloryValue.setText("300 kcal");

        buttonResult = (Button)rootView.findViewById(R.id.buttonResult);
        buttonResult.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RecommendedExercisesActivity.class);
//                Toast.makeText(getActivity().getApplicationContext(), place, Toast.LENGTH_SHORT).show();
                intent.putExtra("place", place);
                intent.putExtra("strength", strength);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId) {
            case R.id.radioButtonInside:
                place = "inside";
                break;
            case R.id.radioButtonOutside:
                place = "outside";
                break;
            case R.id.radioButtonWeak:
                strength = "weak";
                break;
            case R.id.radioButtonMiddle:
                strength = "middle";
                break;
            case R.id.radioButtonStrong:
                strength = "strong";
                break;
        }
    }

}