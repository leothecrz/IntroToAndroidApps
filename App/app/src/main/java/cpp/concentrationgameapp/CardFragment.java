package cpp.concentrationgameapp;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private OnCardClickListener clickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.word);
        textView.setText(word);
        cardBack = view.findViewById(R.id.cardBack);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null)
            clickListener.onCardClick(this);
    }

    public void setClickListener(OnCardClickListener listener) {
        this.clickListener = listener;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void flip() {
        if (flipped) {
            cardBack.setAlpha(1.0f);
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(cardBack, "alpha",
                    1.0f, 0.0f);
            animator.setDuration(250);
            animator.start();

        }
        flipped = !flipped;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public interface OnCardClickListener {
        void onCardClick(CardFragment fragment);
    }
}
