package com.gcode.notes.activities.helpers.main.ui.fab.builders;


import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.fab.FabHelper;
import com.gcode.notes.extras.utils.MyUtils;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class FabSubActionButtonBuilder {
    MainActivity mMainActivity; //used to create image view
    FabHelper mFabHelper; //used as item onClickListener

    private SubActionButton.Builder mItemBuilder;

    public FabSubActionButtonBuilder(MainActivity mainActivity, FabHelper fabHelper) {
        mItemBuilder = new SubActionButton.Builder(mainActivity); //init mItemBuilder

        //!NOTE: fab#getBackground doesn't work correctly before Lollipop
        if (MyUtils.isLollipop()) {
            //its Lollipop or greater version, use fab#getBackground()
            mItemBuilder.setBackgroundDrawable(mainActivity.getFab().getBackground());
        } else {
            //its pre-lollipop, get our sub_menu_background_selector
            mItemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(mainActivity,
                    R.drawable.sub_menu_background_selector));
        }
        //mItemBuild is ready to build SubActionButtons

        //init class variables
        mMainActivity = mainActivity;
        mFabHelper = fabHelper;
    }

    public SubActionButton buildSubActionBtn(int iconResourceId, String tag) {
        //TODO: make subActionButtons bigger so can be clicked easily
        //builds SubAction button with icon from resources
        SubActionButton subActionButton = mItemBuilder.setContentView(createImageView(iconResourceId)).build();
        subActionButton.setOnClickListener(mFabHelper); //sets sub action button onClickListener
        subActionButton.setTag(tag); //sets tag used to identify which sub action button was clicked
        return subActionButton; //returns built sub action button
    }

    private ImageView createImageView(int resourceId) {
        ImageView imageView = new ImageView(mMainActivity); //creates new image view
        imageView.setImageResource(resourceId); //set its image resource
        return imageView;
    }
}
