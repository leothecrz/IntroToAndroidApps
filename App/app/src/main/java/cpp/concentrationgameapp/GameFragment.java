package cpp.concentrationgameapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment implements CardFragment.OnCardClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TableLayout cardGrid;
    private TableRow[] cardRows;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        HighScoreTable.getInstance().clear();
        HighScoreTable.getInstance().isHighScore(4, 2);
        HighScoreTable.getInstance().addHighScore("Test", 4, 2);
        HighScoreTable.getInstance().addHighScore("Test", 4, 1);
        HighScoreTable.getInstance().addHighScore("Test", 4, 2);
        HighScoreTable.getInstance().addHighScore("Test", 4, 5);
        HighScoreTable.getInstance().addHighScore("Test", 4, 5);
        HighScoreTable.getInstance().addHighScore("Test", 4, 7);
        HighScoreTable.getInstance().addHighScore("Test", 6, 7);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: retain card fragments on rotate
        if (savedInstanceState == null) {
            cardGrid = view.findViewById(R.id.cardTable);
            for (int i = 0; i < 4; i++) {
                TableRow row = new TableRow(getActivity());
                row.setGravity(Gravity.CENTER);
                row.setId(View.generateViewId());
                row.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                for (int x = 0; x < 4; x++) {
                    CardFragment cardFragment = CardFragment.newCard("Card " + i + " " + x);
                    cardFragment.setClickListener(this);
                    getChildFragmentManager().beginTransaction()
                            .add(row.getId(), cardFragment, "Card " + i + " " + x)
                            .commit();
                }

                cardGrid.addView(row);
            }
        }
    }

    @Override
    public void onCardClick(CardFragment fragment) {
        Log.i("cpp_concentration", fragment.getWord());
        fragment.flip();
    }
}