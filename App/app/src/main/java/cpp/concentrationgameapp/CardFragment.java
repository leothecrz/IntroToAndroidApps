package cpp.concentrationgameapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CardFragment extends Fragment implements View.OnClickListener {

    private String word;
    private TextView textView;
    private ImageView cardBack;
    private boolean flipped;

    public static CardFragment newCard(String word) {
        CardFragment cardFragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString("word", word);
        cardFragment.setArguments(args);
        return cardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        word = args.getString("word");
        System.out.println(getId());

        return inflater.inflate(R.layout.card_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.word);
        textView.setText(word);
        cardBack = view.findViewById(R.id.cardBack);
        getView().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        System.out.println(word);
        flip();
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void flip() {
        if (flipped) {
            cardBack.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
        } else {
            cardBack.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        flipped = !flipped;
    }
}
