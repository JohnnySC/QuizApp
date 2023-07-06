package com.github.johnnysc.quizapp.views;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * @author Asatryan on 31.07.2022
 */
public class ChoiceButtonState extends View.BaseSavedState {
    String name;
    ChoiceButton.State state;

    ChoiceButtonState(Parcelable superState) {
        super(superState);
    }

    private ChoiceButtonState(Parcel in) {
        super(in);
        name = in.readString();
        state = in.readParcelable(ChoiceButton.State.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(name);
        out.writeParcelable(state, flags);
    }

    public static final Parcelable.Creator<ChoiceButtonState> CREATOR
            = new Parcelable.Creator<ChoiceButtonState>() {
        public ChoiceButtonState createFromParcel(Parcel in) {
            return new ChoiceButtonState(in);
        }

        public ChoiceButtonState[] newArray(int size) {
            return new ChoiceButtonState[size];
        }
    };
}