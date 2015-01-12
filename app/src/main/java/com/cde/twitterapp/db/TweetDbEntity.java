package com.cde.twitterapp.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for Twitter tweet entities to be stored in local db.
 * Created by dello on 31/12/14.
 **/

@DatabaseTable(tableName = "tweets")
public class TweetDbEntity{
    static final String ID_COLUMN_NAME = "id";
    static final String TEXT_COLUMN_NAME = "text";
    static final String AUTHOR_COLUMN_NAME = "author";
    static final String DATE_COLUMN_NAME = "date";
    static final String LATITUDE_COLUMN_NAME = "latitude";
    static final String LONGITUDE_COLUMN_NAME = "longitude";

    @DatabaseField(id = true, columnName = ID_COLUMN_NAME)
    private long id;

    @DatabaseField(columnName = TEXT_COLUMN_NAME)
    private String text;

    @DatabaseField(columnName = AUTHOR_COLUMN_NAME, foreign = true, foreignAutoRefresh = true)
    private UserDbEntity authorEntity;

    @DatabaseField(columnName = DATE_COLUMN_NAME)
    private String date;

    @DatabaseField(columnName = LATITUDE_COLUMN_NAME)
    private Double latitude;

    @DatabaseField(columnName = LONGITUDE_COLUMN_NAME)
    private Double longitude;

    private Boolean checked = false;

    public TweetDbEntity(){
        //Empty constructor needed by ORMLITE
    }

    /**
     *
     * @param id Tweet id (same as in Twitter's db
     * @param text Tweet Content
     * @param authorEntity Author
     * @param date
     * @param location Array list in the form [longitude, latitude]
     */
    public TweetDbEntity(long id, String text, UserDbEntity authorEntity, String date, List<Double> location){
        this.setId(id);
        this.setText(text);
        this.setAuthorEntity(authorEntity);
        this.setDate(date);
        if(location != null && location.size() > 0) {
            this.latitude = location.get(0);
            this.longitude = location.get(1);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDbEntity getAuthorEntity() {
        return authorEntity;
    }

    public void setAuthorEntity(UserDbEntity authorEntity) {
        this.authorEntity = authorEntity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return tweets location as an list in the form [longitude, latitude]
     */
    public List<Double> getLocation() {
        if(latitude == null || longitude == null) return null;
        ArrayList<Double> location = new ArrayList<Double>();
        location.add(0, latitude);
        location.add(1, longitude);
        return location;
    }

    public void setLocation(List<Double> location) {
        if(location != null && location.size() > 0) {
            this.latitude = location.get(0);
            this.longitude = location.get(1);
        }
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public boolean isChecked(){ return checked; }
}
