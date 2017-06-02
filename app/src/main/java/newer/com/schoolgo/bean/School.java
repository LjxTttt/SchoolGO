package newer.com.schoolgo.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/8.
 */

public class School {


    private String notice_content;

    private int school_type;

    private String notice_time;

    private String notice_titile;

    private int notice_id;

    private String content;
    private ArrayList<String> imgSrc;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(ArrayList<String> imgSrc) {
        this.imgSrc = imgSrc;
    }


    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public int getSchool_type() {
        return school_type;
    }

    public void setSchool_type(int school_type) {
        this.school_type = school_type;
    }

    public String getNotice_time() {
        return notice_time;
    }

    public void setNotice_time(String notice_time) {
        this.notice_time = notice_time;
    }

    public String getNotice_titile() {
        return notice_titile;
    }

    public void setNotice_titile(String notice_titile) {
        this.notice_titile = notice_titile;
    }

    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }


}
