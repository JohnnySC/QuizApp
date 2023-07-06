package com.github.johnnysc.quizapp.views;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class NextButtonState extends View.BaseSavedState {

    NextButton.State state;

    NextButtonState(Parcelable superState) {
        super(superState);
    }

    private NextButtonState(Parcel in) {
        super(in);
        state = in.readParcelable(NextButton.State.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeParcelable(state, flags);
    }


    public static final Parcelable.Creator<NextButtonState> CREATOR
            = new Parcelable.Creator<NextButtonState>() {
        public NextButtonState createFromParcel(Parcel in) {
            return new NextButtonState(in);
        }

        public NextButtonState[] newArray(int size) {
            return new NextButtonState[size];
        }
    };
}
