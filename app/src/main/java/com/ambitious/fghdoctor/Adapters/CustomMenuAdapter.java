package com.ambitious.fghdoctor.Adapters;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import nl.psdcompany.duonavigationdrawer.views.DuoOptionView;


public class CustomMenuAdapter extends BaseAdapter {
    private ArrayList<String> mOptions = new ArrayList<>();
    private ArrayList<Drawable> mIcons = new ArrayList<>();
    private ArrayList<DuoOptionView> mOptionViews = new ArrayList<>();

    public CustomMenuAdapter(ArrayList<String> options, ArrayList<Drawable> icons) {
        mOptions = options;
        mIcons = icons;
    }

    @Override
    public int getCount() {
        return mOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mOptions.get(position);
    }

    public void setViewSelected(int position, boolean selected) {

        // Looping through the options in the menu
        // Selecting the chosen option
        for (int i = 0; i < mOptionViews.size(); i++) {
            if (i == position) {
                mOptionViews.get(i).setSelected(selected);
            } else {
                mOptionViews.get(i).setSelected(!selected);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String option = mOptions.get(position);
        final Drawable draw = mIcons.get(position);

        // Using the DuoOptionView to easily recreate the demo
        final DuoOptionView optionView;
        if (convertView == null) {
            optionView = new DuoOptionView(parent.getContext());
        } else {
            optionView = (DuoOptionView) convertView;
        }

        // Using the DuoOptionView's default selectors
        optionView.bind(option, draw, null);

        // Adding the views to an array list to handle view selection
        mOptionViews.add(optionView);

        return optionView;
    }
}
