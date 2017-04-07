package com.example.rohit02kumar.smarttodonavigator.database;

/**
 * Created by rohit02.kumar on 4/7/2017.
 */
public class Event {
    private Integer mId;
    private String mEventName;
    private String mEvenType;
    private long mFromDate;
    private long mToDate;

    public Integer getmId() {
        return mId;
    }

    public Event(Integer mId, String mEventName, String mEvenType, long mFromDate, long mToDate) {
        this.mId = mId;
        this.mEventName = mEventName;
        this.mEvenType = mEvenType;
        this.mFromDate = mFromDate;
        this.mToDate = mToDate;
    }
 Event()
 {

 }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public void setmEvenType(String mEvenType) {
        this.mEvenType = mEvenType;
    }

    public void setmFromDate(long mFromDate) {
        this.mFromDate = mFromDate;
    }

    public void setmToDate(long mToDate) {
        this.mToDate = mToDate;
    }

    public String getmEventName() {
        return mEventName;
    }

    public String getmEvenType() {
        return mEvenType;
    }

    public long getmFromDate() {
        return mFromDate;
    }

    public long getmToDate() {
        return mToDate;
    }
}
