package com.gridone.scraping.component;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gridone.scraping.model.News;
import com.gridone.scraping.service.MailClient;

@Component
public class WebScraping {
	private static String KOREA_COVID_DATAS_URL = "https://search.naver.com/search.naver?query=keyword&where=news&field=1&pd=4";
	private Map<String, News> newsGridone = new HashMap<String, News>();
	private Map<String, News> newsAI = new HashMap<String, News>();
	private Map<String, News> newsOCR = new HashMap<String, News>();
	private Map<String, News> newsRPA = new HashMap<String, News>();
	private Map<String, News> newsAuto = new HashMap<String, News>();
	
	@Value("${scraping.keyword}")
	String keywords;
	
	@Value("${email.to}")
	String emailTo;
	
	@Autowired
	private MailClient mailClient;
	
	@Scheduled(cron="0/30 * * * * *")
	public void shceduledItemList() {
		for (String item : keywords.split(" ")) {
			try {
				getNewsScraping(item.replace("_"," "));
				Thread.sleep((int)(Math.random()*3000)+500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getNewsScraping(String item) {
		try {
			Document document = Jsoup.connect(KOREA_COVID_DATAS_URL.replace("keyword", item)).timeout((int)(Math.random()*5000)+500).get();
			Elements elements = document.select(".list_news > li");
			for(Element element : elements) {
				Elements tmp = element.getElementsByClass("news_tit");
				if (item.equals("그리드원")) {
					if (newsGridone.containsKey(tmp.text())) {
						continue;
					}
					newsGridone.put(tmp.text(), new News(tmp.text(),tmp.attr("href"),element.text()));
				}else if (item.equals("AI 인공지능")) {
					if (newsAI.containsKey(tmp.text())) {
						continue;
					}
					newsAI.put(tmp.text(), new News(tmp.text(),tmp.attr("href"),element.text()));
				}else if (item.equals("OCR")) {
					if (newsOCR.containsKey(tmp.text())) {
						continue;
					}
					newsOCR.put(tmp.text(), new News(tmp.text(),tmp.attr("href"),element.text()));
				}else if (item.equals("RPA")) {
					if (newsRPA.containsKey(tmp.text())) {
						continue;
					}
					newsRPA.put(tmp.text(), new News(tmp.text(),tmp.attr("href"),element.text()));
				}else if (item.equals("업무자동화")) {
					if (newsAuto.containsKey(tmp.text())) {
						continue;
					}
					newsAuto.put(tmp.text(), new News(tmp.text(),tmp.attr("href"),element.text()));
				}
			}	
			document.clearAttributes();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Scheduled(cron="0  30  8  *  *  *")
	public void sendEmail() {
		StringBuffer temp = new StringBuffer();
		temp.append("<table class='table'>");
		for (String item : keywords.split(" ")) {
			item = item.replace("_", " ");
			temp.append("<tr style='background-color: #FFC107;'><th colspan='2'>"+item+" 네이버 뉴스 </th></tr>");
			temp.append("<tr><th style='width: 200px;'>제목</th><th>요약</th></tr>");
			if (item.equals("그리드원")) {
				if (newsGridone.size() == 0) {
					temp.append("<tr ><td colspan='2'>"+item+" 의 소식이 없습니다. </td></tr>");
				}else {
					for (String item2 : newsGridone.keySet()) {
						temp.append("<tr >");
						temp.append("<td ><a href='"+newsGridone.get(item2).getLink()+"'>"+newsGridone.get(item2).getTitle()+"</a></td>");
						temp.append("<td>"+newsGridone.get(item2).getContent()+"</td>");
						temp.append("</tr>");
					}					
				}
				newsGridone.clear();
			}else if (item.equals("OCR")) {
				if (newsOCR.size() == 0) {
					temp.append("<tr ><td colspan='2'>"+item+" 의 소식이 없습니다. </td></tr>");
				}else {
					for (String item2 : newsOCR.keySet()) {
						temp.append("<tr >");
						temp.append("<td><a href='"+newsOCR.get(item2).getLink()+"'>"+newsOCR.get(item2).getTitle()+"</a></td>");
						temp.append("<td>"+newsOCR.get(item2).getContent()+"</td>");
						temp.append("</tr>");
					}
				}
				newsOCR.clear();
			}else if (item.equals("RPA")) {
				if (newsRPA.size() == 0) {
					temp.append("<tr ><td colspan='2'>"+item+" 의 소식이 없습니다. </td></tr>");
				}else {
					for (String item2 : newsRPA.keySet()) {
						temp.append("<tr >");
						temp.append("<td><a href='"+newsRPA.get(item2).getLink()+"'>"+newsRPA.get(item2).getTitle()+"</a></td>");
						temp.append("<td>"+newsRPA.get(item2).getContent()+"</td>");
						temp.append("</tr>");
					}
				}
				newsRPA.clear();
			}else if (item.equals("업무자동화")) {
				if (newsAuto.size() == 0) {
					temp.append("<tr ><td colspan='2'>"+item+" 의 소식이 없습니다. </td></tr>");
				}else {
					for (String item2 : newsAuto.keySet()) {
						temp.append("<tr >");
						temp.append("<td><a href='"+newsAuto.get(item2).getLink()+"'>"+newsAuto.get(item2).getTitle()+"</a></td>");
						temp.append("<td>"+newsAuto.get(item2).getContent()+"</td>"	);
						temp.append("</tr>");
					}
				}
				newsAuto.clear();
			}else if (item.equals("AI 인공지능")) {
				if (newsAI.size() == 0) {
					temp.append("<tr ><td colspan='2'>"+item+" 의 소식이 없습니다. </td></tr>");
				}else {
					for (String item2 : newsAI.keySet()) {
						temp.append("<tr >");
						temp.append("<td><a href='"+newsAI.get(item2).getLink()+"'>"+newsAI.get(item2).getTitle()+"</a></td>");
						temp.append("<td>"+newsAI.get(item2).getContent()+"</td>");
						temp.append("</tr>");
					}
				}
				newsAI.clear();
			}
		}
		temp.append("</table>");
		mailClient.prepareAndSend(emailTo, temp.toString());
	}
}
