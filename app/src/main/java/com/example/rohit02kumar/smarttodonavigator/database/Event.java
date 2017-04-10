package com.example.rohit02kumar.smarttodonavigator.database;

/**
 * Created by rohit02.kumar on 4/7/2017.
 */
public class Event {
//    private Integer mId;
    private String mEventName;
    private String mEvenType;
    private String mFromDate;
    private String mToDate;
    private int isComplete;

//    public Integer getmId() {
//        return mId;
//    }

    public Event(Integer mId, String mEventName, String mEvenType, String mFromDate, String mToDate) {
//        this.mId = mId;
        this.mEventName = mEventName;
        this.mEvenType = mEvenType;
        this.mFromDate = mFromDate;
        this.mToDate = mToDate;
    }
 Event()
 {

 }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }
//    public void setmId(Integer mId) {
//        this.mId = mId;
//    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public void setmEvenType(String mEvenType) {
        this.mEvenType = mEvenType;
    }

    public void setmFromDate(String mFromDate) {
        this.mFromDate = mFromDate;
    }

    public void setmToDate(String mToDate) {
        this.mToDate = mToDate;
    }

    public String getmEventName() {
        return mEventName;
    }

    public String getmEvenType() {
        return mEvenType;
    }

    public String getmFromDate() {
        return mFromDate;
    }

    public String getmToDate() {
        return mToDate;
    }
}
