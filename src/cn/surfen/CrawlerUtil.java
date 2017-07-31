package cn.surfen;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CrawlerUtil {
	private static Boolean isToday = true;// 判断是否为今天
	private static List<House> houses = new ArrayList<House>();
	private static final String URL = "https://hz.lianjia.com/";
	private static final String TIME = getDate();// 获取今天时间
	private static final String ROOT = "chengjiao/pg";// 根目录
	private static int page = 1;// 分页

	/**
	 * 单例工具类
	 */
	private CrawlerUtil() {

	}

	/**
	 * 获取每日房产信息
	 */
	public static List<House> getHouse() {
		while (isToday) {
			String html = pickData(URL + ROOT + page);
			System.out.println("当前网页为" + URL + ROOT + page);
			toDealWith(html);
			page++;
			System.out.println("翻页");
		}
		System.out.println("查询完毕");
		return houses;
	}

	/**
	 * 获取基本网络信息
	 * 
	 * @param url
	 * @return
	 */
	private static String pickData(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				// 打印响应状态
				if (entity != null) {
					return EntityUtils.toString(entity);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 处理信息获取链接
	 */
	private static void toDealWith(String html) {
		Document document = Jsoup.parse(html);
		Element elements = document.getElementsByClass("listContent").get(0);
		// 获得子节点
		Elements li = elements.children();

		for (int i = 0; i < li.size(); i++) {

			// 判断日期是否相等
			if (!li.get(i).getElementsByClass("dealDate").get(0).html().equals(TIME)) {
				isToday = false;
				break;
			}

			String imageUrl = getImageUrl(li.get(i));
			String information = getNumber(li.get(i));
			String herf = getHerf(li.get(i));
			String community = getCommunity(herf);
			House house = new House(information, imageUrl, community, TIME);
			houses.add(house);
			try {
				int sleep = (int) (Math.random() * 5 + 5);
				System.out.println("休息" + sleep + "s");
				Thread.sleep(sleep * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 获取当前日期
	 */
	private static String getDate() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		return formatter.format(date);
		// return "2017.07.16";
	}

	/**
	 * 获得imageurl
	 */
	private static String getImageUrl(Element li) {

		String url = li.select("img[src]").toString();
		if (url.indexOf("data-original=\"") != -1) {
			url = url.substring(url.indexOf("data-original=\""));
			url = url.substring(url.indexOf("h"), url.indexOf("\" alt"));
		}
		if (url.equals("")) {
			url = null;
		}
		return url;
	}

	/**
	 * 获得成交价
	 */
	private static String getNumber(Element li) {
		return li.getElementsByClass("number").get(0).html();
	}

	/**
	 * 获得详情地址
	 */
	private static String getHerf(Element li) {
		// System.out.println(li.html());
		String url = li.select("a[href]").toString();
		if (url.indexOf("href=\"") != -1) {
			url = url.substring(url.indexOf("href=\""));
			url = url.substring(url.indexOf("http"), url.indexOf("\" target"));
		}
		return url;
	}

	/**
	 * 获得房子详情
	 */
	private static String getCommunity(String url) {
		String html = pickData(url);
		Document document = Jsoup.parse(html);
		Elements elements = document.getElementsByClass("deal-bread");
		// elements.get
		Elements li = elements.get(0).select("a");
		li.remove(0);
		StringBuffer info = new StringBuffer();
		for (int i = 0; i < 3; i++) {
			String s = li.get(i).html();
			info.append(s.substring(0, s.indexOf("二")));
		}

		return info.toString();
	}
}
