package newer.com.schoolgo.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import newer.com.schoolgo.bean.SchoolDetail;

/**
 * Created by Administrator on 2017/4/25.
 */

public class JsoupUtil {
    public static SchoolDetail parseSchool_News(String html) {
        Document document = Jsoup.parse(html);
        return pasreDocument(document);
    }

    private static SchoolDetail pasreDocument(Document document) {
        SchoolDetail schoolDetail = new SchoolDetail();
        Element element = document.getElementById("content");
        if (element.getElementsByTag("h1").hasText()) {
            schoolDetail.setTitle("h1", element.getElementsByTag("h1").text());
        }
        if (element.getElementsByTag("h2").hasText()) {
            schoolDetail.setTitle("h2", element.getElementsByTag("h2").text());
        }
        if (element.getElementsByTag("h3").hasText()) {
            schoolDetail.setTitle("h3", element.getElementsByTag("h3").text());
        }
        Element content = element.getElementById("content_article");
        //设置编辑人信息
        schoolDetail.setEdit_man(content.getElementsByClass("p_right").text());
        //清除编辑人标签
        content.getElementsByClass("p_right").first().text("");
        Elements contentEms = content.getElementsByTag("p").attr("style", "text-indent:2em");
        StringBuilder sBuilder = new StringBuilder();
        ArrayList<String> imgSrc = new ArrayList<>();
        for (Element contents : contentEms) {
            if (contents.hasText()) {
                sBuilder.append("    " + contents.text() + "\n\n");
            }
            if (contents.getElementsByTag("img").hasAttr("src")) {
                imgSrc.add(contents.getElementsByTag("img").attr("src"));
            }
        }
        schoolDetail.setImgSrc(imgSrc);
        schoolDetail.setContent(sBuilder.toString());
        return schoolDetail;
    }

    public static SchoolDetail connectParse(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pasreDocument(document);
    }
}
