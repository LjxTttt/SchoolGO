package newer.com.schoolgo.bean;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Coment {

    private String coment_name;
    private String coment_content;

    public Coment() {
    }

    public Coment(String userName, String content) {
        this.coment_name = userName;
        this.coment_content = content;
    }

    public String getComent_name() {
        return coment_name;
    }

    public void setComent_name(String coment_name) {
        this.coment_name = coment_name;
    }

    public String getComent_content() {
        return coment_content;
    }

    public void setComent_content(String coment_content) {
        this.coment_content = coment_content;
    }
}
