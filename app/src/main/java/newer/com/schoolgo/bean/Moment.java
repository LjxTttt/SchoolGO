package newer.com.schoolgo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/10 上午1:12
 * 描述:
 */
public class Moment implements Serializable {
    public ArrayList<String> moment_Img;
    public String moment_user_name;
    public String moment_content;
    public long moment_tag;
    public List<Coment> coment = new ArrayList<>();

    public Moment() {
    }

    public Moment(String content, ArrayList<String> photos) {
        this.moment_content = content;
        this.moment_Img = photos;
    }


}