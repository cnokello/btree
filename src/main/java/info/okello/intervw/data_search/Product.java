package info.okello.intervw.data_search;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private long productId;
	private String name;
	private double price;

	public Product(long productId, String name, double price) {
		super();
		this.productId = productId;
		this.name = name;
		this.price = roundPrice(price, 2);
	}

	@Override
	public String toString() {
		return "[Product ID = " + productId + "\t Name = " + name
				+ "\t Price = " + price + "]";
	}

	private static double roundPrice(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
