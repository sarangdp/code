package nyse.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NYSEParser {
	private String stockTicker;
	private String tradeDate;

	private Float openPrice;
	private Float highPrice;
	private Float lowPrice;
	private Float closePrice;
	private Long volume;

	public void parseRecord(String record) {
		String[] fields = record.split(",");

		stockTicker = fields[0];
		tradeDate = fields[1];
		openPrice = new Float(fields[2]);
		highPrice = new Float(fields[3]);
		lowPrice = new Float(fields[4]);
		closePrice = new Float(fields[5]);
		volume = new Long(fields[6]);
	}

	public String getTradeMonth() {
		SimpleDateFormat origibalDateDF = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat targetDateDF = new SimpleDateFormat("yyyy-MM");

		Date origDate = new Date();

		try {
			origDate = origibalDateDF.parse(this.tradeDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return targetDateDF.format(origDate);
	}

	public Long getTradeDateNumeric() {
		SimpleDateFormat origibalDateDF = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat targetDateDF = new SimpleDateFormat("yyyyMMdd");

		Date origDate = new Date();

		try {
			origDate = origibalDateDF.parse(this.tradeDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Long tradeDateNumeric = new Long(targetDateDF.format(origDate));
		
		return tradeDateNumeric;
	}
	
	public String getStockTicker() {
		return stockTicker;
	}

	public void setStockTicker(String stockTicker) {
		this.stockTicker = stockTicker;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public Float getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Float openPrice) {
		this.openPrice = openPrice;
	}

	public Float getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(Float highPrice) {
		this.highPrice = highPrice;
	}

	public Float getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(Float lowPrice) {
		this.lowPrice = lowPrice;
	}

	public Float getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Float closePrice) {
		this.closePrice = closePrice;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

}
