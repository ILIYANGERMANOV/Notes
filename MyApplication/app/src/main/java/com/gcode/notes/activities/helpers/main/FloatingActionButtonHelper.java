package com.gcode.notes.activities.helpers.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.compose.ComposeListActivity;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.animations.MyAnimator;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.VoiceUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Tags;
import com.gcode.notes.ui.helpers.DialogHelper;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class FloatingActionButtonHelper implements View.OnClickListener {
    MainActivity mMainActivity;

    public FloatingActionButtonHelper(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void setupFloatingActionButtonMenu() {
        //TODO: Refactor and optimize, fix onClick() spam problems (prelolipop click events isn't handled, lolipop random option is selected)
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(mMainActivity);

        //TODO: make subActionButtons bigger so can be clicked easily
        if (Build.VERSION.SDK_INT >= 21) {
            itemBuilder.setBackgroundDrawable(mMainActivity.getFab().getBackground());
        } else {
            itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(mMainActivity,
                    R.drawable.sub_menu_background_selector));
        }


        SubActionButton textNoteButton = itemBuilder.setContentView(createImageView(R.drawable.ic_note_add_white_24dp)).build();

        SubActionButton listButton = itemBuilder.setContentView(createImageView(R.drawable.ic_list_white_24dp)).build();

        SubActionButton voiceButton = itemBuilder.setContentView(
                createImageView(R.drawable.ic_keyboard_voice_white_24dp)).build();

        SubActionButton cameraButton = itemBuilder.setContentView(
                createImageView(R.drawable.ic_photo_camera_white_24dp)).build();

        textNoteButton.setOnClickListener(this);
        listButton.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);

        textNoteButton.setTag(Tags.TAG_TEXT_NOTE);
        listButton.setTag(Tags.TAG_LIST);
        voiceButton.setTag(Tags.TAG_VOICE_NOTE);
        cameraButton.setTag(Tags.TAG_CAMERA);

        MainActivity.mActionMenu = new FloatingActionMenu.Builder(mMainActivity)
                .addSubActionView(textNoteButton)
                .addSubActionView(listButton)
                .addSubActionView(voiceButton)
                .addSubActionView(cameraButton)
                .attachTo(mMainActivity.getFab())
                .build();

        MainActivity.mActionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                mMainActivity.getFab().setImageResource(R.drawable.ic_close_white_24dp);
                MyAnimator.startAnimation(mMainActivity, mMainActivity.getFab(), R.anim.open_rotate_anim);
                mMainActivity.mSubMenuOpened = true;
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                mMainActivity.getFab().setImageResource(R.drawable.ic_open_white_24dp);
                MyAnimator.startAnimation(mMainActivity, mMainActivity.getFab(), R.anim.close_rotate_anim);
                mMainActivity.mSubMenuOpened = false;
            }
        });

        mMainActivity.getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTranslationY() > Constants.FAB_THRESHOLD_TRANSLATION_Y) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", v.getTranslationY(), 0);
                        anim.start();
                    }
                    v.setTranslationY(0);
                }
                if (MainActivity.mActionMenu != null) {
                    MainActivity.mActionMenu.toggle(true);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.mActionMenu != null && mMainActivity.mSubMenuOpened && !MainActivity.mActionMenu.isOpen()) {
                    mMainActivity.getDrawer().setTranslationY(0);
                    MainActivity.mActionMenu.open(false);
                }
            }
        }, 50);
    }

    private ImageView createImageView(int resourceId) {
        ImageView imageView = new ImageView(mMainActivity);
        imageView.setImageResource(resourceId);
        return imageView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == null) return;

        Intent intent;

        String tag = (String) v.getTag();
        switch (tag) {
            case Tags.TAG_TEXT_NOTE:
                intent = new Intent(mMainActivity, ComposeNoteActivity.class);
                intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_ZERO);
                mMainActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
                break;
            case Tags.TAG_LIST:
                intent = new Intent(mMainActivity, ComposeListActivity.class);
                //TODO: add EXTRA_SETUP_FROM if works well
                mMainActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
                break;
            case Tags.TAG_VOICE_NOTE:
                VoiceUtils.promptSpeechInput(mMainActivity);
                break;
            case Tags.TAG_CAMERA:
                DialogHelper.buildAddPictureDialog(mMainActivity);
                break;
        }

        if (MainActivity.mActionMenu != null) {
            MainActivity.mActionMenu.toggle(false);   //can cause problems with startActivityForResult() before it
        } else {
            MyDebugger.log("FloatingActionButtonHelper: onClick(): mActionMenu is null.");
        }
    }
}
