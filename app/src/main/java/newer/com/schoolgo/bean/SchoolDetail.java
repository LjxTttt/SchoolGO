package newer.com.schoolgo.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SchoolDetail {

    private Map<String, String> title = new HashMap<>();
    private String content;
    private ArrayList<String> imgSrc;
    private String edit_man;
    private String edit_time;

    public List<String> getTitle() {
        List<String> listTile = new ArrayList<>();
        if (title.containsKey("h1")) {
            listTile.add(title.get("h1"));
        }
        if (title.containsKey("h2")) {
            listTile.add(title.get("h2"));
        }
        if (title.containsKey("h3")) {
            listTile.add(title.get("h3"));
        }
        return listTile;
    }

    public void setTitle(String key, String value) {
        this.title.put(key, value);
    }

    @Override
    public String toString() {
        return "SchoolDetail{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imgSrc=" + imgSrc +
                ", edit_man='" + edit_man + '\'' +
                ", edit_time='" + edit_time + '\'' +
                '}';
    }

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

    public String getEdit_man() {
        return edit_man;
    }

    public void setEdit_man(String edit_man) {
        this.edit_man = edit_man;
    }

    public String getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(String edit_time) {
        this.edit_time = edit_time;
    }
}
