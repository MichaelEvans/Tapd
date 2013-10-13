package com.tappd.fancy;

import com.tappd.model.Order;

public class ExpandableListItem implements OnSizeChangedListener {

    //private String mTitle;
    //private String mText;
    private boolean mIsExpanded;
    private int mImgResource;
    private int mCollapsedHeight;
    private int mExpandedHeight;
    private Order mOrder;

    public ExpandableListItem(Order order, int collapsedHeight) {
        //mTitle = title;
        mCollapsedHeight = collapsedHeight;
        mIsExpanded = false;
        //mText = text;
        mExpandedHeight = -1;
        mOrder = order;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

//    public String getTitle() {
//        return mTitle;
//    }

    public int getImgResource() {
        return mImgResource;
    }

    public int getCollapsedHeight() {
        return mCollapsedHeight;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        mCollapsedHeight = collapsedHeight;
    }

//    public String getText() {
//        return mText;
//    }
//
//    public void setText(String text) {
//        mText = text;
//    }

    public int getExpandedHeight() {
        return mExpandedHeight;
    }

    public void setExpandedHeight(int expandedHeight) {
        mExpandedHeight = expandedHeight;
    }

    @Override
    public void onSizeChanged(int newHeight) {
        setExpandedHeight(newHeight);
    }
    
    public Order getOrder(){
    	return mOrder;
    }
}
