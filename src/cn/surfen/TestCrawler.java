package cn.surfen;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestCrawler {
	public static void main(String[] args) {
		List<House> houses = CrawlerUtil.getHouse();

		/**
		 * 获得当前时间
		 */
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");

		String time = formatter.format(date) + "日房屋成交信息.txt";

		try {
			BufferedWriter bw = new BufferedWriter(
					new FileWriter("C:\\Documents and Settings\\Administrator\\桌面\\" + time));

			for (House h : houses) {
				bw.write(h.toString());
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
