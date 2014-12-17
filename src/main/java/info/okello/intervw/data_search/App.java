package info.okello.intervw.data_search;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

/**
 * 
 * @author nelson.okello
 * @version 1.0.0
 * @created 17-Dec-2014
 * 
 *          Tester
 * 
 *          INDEXING OR CREATING THE BINARY TREE: An indexing service is called.
 *          The indexing service creates 1000000 random products. On my PC,
 *          building a binary tree with the 1000000 products take just over 3
 *          seconds
 * 
 *          SAMPLE PRODUCTS TO TEST HOW LONG SEARCH TAKES: It then prints out a
 *          few sample products that you can use to test how fast the search is.
 * 
 *          SEARCH CONSOLE: The program presents you with a console for
 *          searching products
 * 
 *          SEARCH RESULT: The result of each search include the matched product
 *          and time (in microseconds) that it took to perform the search. The
 *          worst case time complexity of the search is O(logN).
 *
 */
public class App {
	public static void main(String[] args) {
		// Index products
		BTree<Integer, Product> bTree = indexProducts();

		// Interactive search console
		Scanner scanner = new Scanner(System.in);
		boolean more = true;
		while (more) {
			System.out
					.print("\nEnter ID of the product you wish to search (0 to terminate): ");

			String idStr = scanner.next();
			int id = 0;
			try {
				id = Integer.parseInt(idStr);
			} catch (Exception e) {
				System.out.println("ERROR: Product ID must be an integer value");
				continue;
			}
			// int id = scanner.nextInt();
			if (id <= 0)
				more = false;
			else {
				long searchStart = System.nanoTime();
				List<Product> products = bTree.lookup(id);
				long searchStop = System.nanoTime();

				System.out.println("SEARCH RESULT:");
				if (products == null || products.size() == 0)
					System.out.println("No matching products found");
				else {
					for (Product product : products) {
						System.out.println(product.toString());
					}
				}

				System.out.println(String.format("TIME TAKEN: %s microseconds",
						(searchStop - searchStart) / 1000));
			}
		}

		System.out.println("Program terminated.");
	}

	/**
	 * @author nelson.okello
	 * @version 1.0.0
	 * @created 17-Dec-2014
	 * 
	 *          Creates 1000000 sample products randomly and indexes them by
	 *          building a binary tree
	 * 
	 * @return Returns the built binary tree
	 * 
	 */
	private static BTree<Integer, Product> indexProducts() {
		long start = new java.util.Date().getTime();
		System.out.println("\nIndexing 1,000,000 products...");
		// Instantiate the binary tree with Integer type key and Product type
		// value.
		BTree<Integer, Product> bTree = new BTree<Integer, Product>();

		Random generator = new Random();
		double priceLowerLimit = 100d;
		double priceUpperLimit = 500d;

		System.out
				.println("\nPRODUCTS SELECTED AT RANDOM FOR YOUR LOOKUP TEST:");
		System.out
				.println("NOTE: Use Product ID = 5 to test that multiple products with the same Product ID can be indexed and retrieved.");

		// Sample data to test products multiple products with the same Product
		// ID
		Product product1 = new Product(5, "Sample product 1", 2.30d);
		Product product2 = new Product(5, "Sample product 2", 4.50d);
		bTree.insert(5, product1);
		bTree.insert(5, product2);

		for (int i = 0; i < 1000000; i++) {
			int productId = generator.nextInt(1000000);
			double price = priceLowerLimit
					+ (generator.nextDouble() * (priceUpperLimit - priceLowerLimit));
			String productName = UUID.randomUUID().toString();

			// Create a random products and insert into the binary tree
			Product product = new Product(productId, productName, price);
			bTree.insert(productId, product);

			// Output sample products for lookup tests.
			if (i % 50000 == 0)
				System.out.println(product.toString());
		}

		long stop = new java.util.Date().getTime();
		System.out.println(String.format(
				"\nIndexing 1,000,000 products completed in %s ms.\n",
				(stop - start)));

		return bTree;
	}
}
