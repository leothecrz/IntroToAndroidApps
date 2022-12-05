package cpp.concentrationgameapp;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
    private ImageView cardBackground;
    private boolean flipped;
    private boolean flippable;
    private int row;
    private int column;
    private OnCardClickListener clickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("word", word);
        outState.putBoolean("flipped", flipped);
        outState.putBoolean("flippable", flippable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.word);
        textView.setText(word);
        cardBack = view.findViewById(R.id.cardBack);
        cardBackground = view.findViewById(R.id.cardBackground);
        view.setOnClickListener(this);

        if (savedInstanceState != null) {
            setWord(savedInstanceState.getString("word"));
            flipped = savedInstanceState.getBoolean("flipped", flipped);
            flippable = savedInstanceState.getBoolean("flippable", flippable);
            if (flipped)
                cardBack.setAlpha(0.0f);
        }
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null)
            clickListener.onCardClick(this);
    }

    /**
     * Sets the click listener for this card
     * @param listener The click listener
     */
    public void setClickListener(OnCardClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * @return True if this card is flipped, false otherwise
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Flips the card
     */
    public void flip() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(cardBack, "alpha",
                flipped ? 0 : 1, flipped ? 1 : 0);
        animator.setDuration(250);
        animator.start();
        flipped = !flipped;
    }

    /**
     * Returns the word set for the card
     * @return The word set for the card
     */
    public String getWord() {
        return word;
    }

    /**
     * Sets a word for this card
     * @param word The word to set
     */
    public void setWord(String word) {
        this.word = word;
        if (textView != null)
            textView.setText(word);
    }

    /**
     * Returns if this card is flippable
     * @return True if this card is flippable, false otherwise
     */
    public boolean isFlippable() {
        return flippable;
    }

    /**
     * @param flippable
     */
    public void setFlippable(boolean flippable) {
        this.flippable = flippable;
    }

    /**
     * Sets the position of this card on the board
     * @param row The row to set
     * @param column The column to set
     */
    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns this card's row
     * @return The row of this card
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns this card's column
     * @return The column of this card
     */
    public int getColumn() {
        return column;
    }

    /**
     * Resets the flip state of this card
     */
    public void reset() {
        flippable = true;
        flipped = false;
        if (cardBack != null)
            cardBack.setAlpha(1.0f);
    }

    public interface OnCardClickListener {
        void onCardClick(CardFragment fragment);
    }
}
