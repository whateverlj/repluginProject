/*******************************************************************************
 *
 * Copyright (c) Weaver Info Tech Co. Ltd
 *
 * Dialog
 *
 * app.ui.Dialog.java
 * TODO: File description or class description.
 *
 * @author: gao_chun
 * @since:  2014-9-03
 * @version: 1.0.0
 *
 * @changeLogs:
 *     1.0.0: First created this class.
 *
 ******************************************************************************/
package com.example.myplugindemo;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author gao_chun
 *
 */
@SuppressLint("NewApi")
public class AlertDialog extends Dialog implements OnClickListener, OnItemClickListener {

    private TextView mTitleTextView;
    private TextView mMessageTextView;

    private Button mNegativeButton;
    private Button mNeutralButton;
    private Button mPositiveButton;

    private View mLeftLineView;
    private View mRightLineView;

    private ListView mListView;
    private ViewGroup mContent;

    private OnClickListener mNegativeListener;
    private OnClickListener mNeutraListener;
    private OnClickListener mPositiveListener;

    private CharSequence[] mItems;
    private OnClickListener mSingleChoiceClickListener;
    private OnMultiChoiceClickListener mMultiChoiceClickListener;
    private int mCheckedItem;
    private boolean[] mCheckedItems;
    private boolean mIsMultiChoice = false;
    private boolean mIsSingleChoice = false;

    /**
     * @param context
     */
    public AlertDialog(Context context) {
        super(context);
        setupViews();
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_positive:
                if (mPositiveListener != null) {
                    mPositiveListener.onClick(this, 0);
                } else {
                    cancel();
                }
                break;

            case R.id.button_negative:
                if (mNegativeListener != null) {
                    mNegativeListener.onClick(this, 1);
                } else {
                    cancel();
                }
                break;

            case R.id.button_nuetral:
                if (mNeutraListener != null) {
                    mNeutraListener.onClick(this, 2);
                } else {
                    cancel();
                }
                break;

            default:
                break;
        }
    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (mIsSingleChoice && mSingleChoiceClickListener != null) {
            mSingleChoiceClickListener.onClick(this, position);
        } // else ignored

        if (mIsMultiChoice && mMultiChoiceClickListener != null) {
            // TODO
        } // else ignored
    }

    private void setupViews() {
        setContentView(R.layout.dialog);

        mTitleTextView = (TextView) findViewById(R.id.text_title);
        mMessageTextView = (TextView) findViewById(R.id.text_message);

        mNegativeButton = (Button) findViewById(R.id.button_negative);
        mNeutralButton = (Button) findViewById(R.id.button_nuetral);
        mPositiveButton = (Button) findViewById(R.id.button_positive);

        mLeftLineView = findViewById(R.id.view_line_left);
        mRightLineView = findViewById(R.id.view_line_right);

        mListView = (ListView) findViewById(R.id.list);

        mContent = (ViewGroup) findViewById(R.id.layout_content);

        mNegativeButton.setOnClickListener(this);
        mNeutralButton.setOnClickListener(this);
        mPositiveButton.setOnClickListener(this);
    }

    private void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    private void setIcon(Drawable icon) {
        mTitleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
    }

    private void setMessage(CharSequence message) {
        mMessageTextView.setVisibility(View.VISIBLE);
        //mMessageTextView.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
        mMessageTextView.setText(message);
    }

    private void setView(View view) {
        if (view != null) {
            final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            mContent.addView(view, layoutParams);
        }
    }

    private void setNegativeButton(CharSequence text, OnClickListener listener) {
        mNegativeButton.setVisibility(View.VISIBLE);
        mNegativeButton.setText(text);
        this.mNegativeListener = listener;
    }

    private void setNeutralButton(CharSequence text, OnClickListener listener) {
        mNeutralButton.setVisibility(View.VISIBLE);
        mNeutralButton.setText(text);
        this.mNeutraListener = listener;
    }

    private void setPositiveButton(CharSequence text, OnClickListener listener) {
        mPositiveButton.setVisibility(View.VISIBLE);
        mPositiveButton.setText(text);
        this.mPositiveListener = listener;
    }

    private void setButtonLines(boolean left, boolean right) {
        mLeftLineView.setVisibility(left ? View.VISIBLE : View.GONE);
        mRightLineView.setVisibility(right ? View.VISIBLE : View.GONE);
    }

    private void setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener listener) {
        mItems = items;
        mSingleChoiceClickListener = listener;
        mCheckedItem = checkedItem;
        mIsSingleChoice = true;
        if (items != null && items.length > 0) {
            mListView.setVisibility(View.VISIBLE);
            final DialogAdapter adapter = new DialogAdapter(getContext(), items);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(this);
            mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListView.setSelection(mCheckedItem);
        }
    }

    private void setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
            final OnMultiChoiceClickListener listener) {
        mItems = items;
        mMultiChoiceClickListener = listener;
        mCheckedItems = checkedItems;
        mIsMultiChoice = true;
    }

    public static class Builder {

        private AlertDialog mAlertDialog;

        private final Context mContext;
        private CharSequence mTitle;
        private Drawable mIconDrawable;
        private CharSequence mMessage;

        private CharSequence mNegative;
        private CharSequence mNeutral;
        private CharSequence mPositive;

        private OnClickListener mNegativeListener;
        private OnClickListener mNeutraListener;
        private OnClickListener mPositiveListener;

        private View mView;

        private CharSequence[] mItems;
        private OnClickListener mSingleChoiceClickListener;
        private OnMultiChoiceClickListener mMultiChoiceClickListener;
        private int mCheckedItem;
        private boolean[] mCheckedItems;
        private boolean mIsMultiChoice = false;
        private boolean mIsSingleChoice = false;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public Builder setTitle(int titleId) {
            setTitle(mContext.getString(titleId));
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public Builder setMessage(int messageId) {
            setMessage(mContext.getString(messageId));
            return this;
        }

        public Builder setIcon(Drawable icon) {
            this.mIconDrawable = icon;
            return this;
        }

        public Builder setIcon(int iconId) {
            setIcon(mContext.getResources().getDrawable(iconId));
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener listener) {
            mItems = items;
            mSingleChoiceClickListener = listener;
            mCheckedItem = checkedItem;
            mIsSingleChoice = true;
            return this;
        }

        public void setView(View view) {
            mView = view;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
                final OnMultiChoiceClickListener listener) {
            mItems = items;
            mMultiChoiceClickListener = listener;
            mCheckedItems = checkedItems;
            mIsMultiChoice = true;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            this.mNegative = text;
            this.mNegativeListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, OnClickListener listener) {
            setNegativeButton(mContext.getString(textId), listener);
            return this;
        }

        public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
            this.mNeutral = text;
            this.mNeutraListener = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, OnClickListener listener) {
            setNeutralButton(mContext.getString(textId), listener);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            this.mPositive = text;
            this.mPositiveListener = listener;
            return this;
        }

        public Builder setPositiveButton(int textId, OnClickListener listener) {
            setPositiveButton(mContext.getString(textId), listener);
            return this;
        }

        public AlertDialog create() {
            final AlertDialog alertDialog = new AlertDialog(mContext);

            if (mTitle != null) {
                alertDialog.setTitle(mTitle.toString());
            } // else ignored

            if (mMessage != null) {
                alertDialog.setMessage(mMessage);
            } // else ignored

            if (mIconDrawable != null) {
                alertDialog.setIcon(mIconDrawable);
            } // else ignored

            if (mNegative != null) {
                alertDialog.setNegativeButton(mNegative, mNegativeListener);
            } // else ignored

            if (mNeutral != null) {
                alertDialog.setNeutralButton(mNeutral, mNeutraListener);
            } // else ignored

            if (mPositive != null) {
                alertDialog.setPositiveButton(mPositive, mPositiveListener);
            } // else ignored

            // 按钮的分隔线
            if (mPositive != null && mNeutral != null && mNegative != null) {
                alertDialog.setButtonLines(true, true);
            } else if (mPositive != null && mNeutral != null) {
                alertDialog.setButtonLines(true, false);
            } else if (mNeutral != null && mNegative != null) {
                alertDialog.setButtonLines(false, true);
            } else if (mPositive != null && mNegative != null) {
                alertDialog.setButtonLines(true, false);
            } else {
                alertDialog.setButtonLines(false, false);
            }

            if (mItems != null && mItems.length > 0) {
                if (mIsSingleChoice) {
                    alertDialog.setSingleChoiceItems(mItems, mCheckedItem, mSingleChoiceClickListener);
                } // else ignored

                if (mIsMultiChoice) {
                    alertDialog.setMultiChoiceItems(mItems, mCheckedItems, mMultiChoiceClickListener);
                } // else ignored
            }  // else ignored

            if (mView != null) {
                alertDialog.setView(mView);
            }

            mAlertDialog = alertDialog;
            return alertDialog;
        }

        public AlertDialog show() {
            create();
            mAlertDialog.show();
            mAlertDialog.setCanceledOnTouchOutside(false);  //设置点击屏幕其他位置无效
            return mAlertDialog;
        }
    }

    public static class DialogAdapter extends ArrayAdapter<CharSequence> {

        /**
         * @param context
         * @param resource
         */
        public DialogAdapter(Context context, CharSequence[] items) {
            super(context, R.layout.list_item_dialog, R.id.text, items);
        }
    }
}
