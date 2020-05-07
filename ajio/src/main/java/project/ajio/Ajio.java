package project.ajio;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Ajio {
	
	public static WebDriver driver;
	public static WebDriverWait wait;
	
    public static void main( String[] args ) throws InterruptedException {
        
    	System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
    	System.setProperty("webdriver.chrome.silentOutput", "true");
    	
    	ChromeOptions option = new ChromeOptions();
    	option.addArguments("--disable-notifications");
    	option.addArguments("--start-maximized");
    	 
    	driver = new ChromeDriver(option);
    	wait = new WebDriverWait(driver, 10);
    	
    	//1) Go to https://www.ajio.com/shop/sale
    	
    	driver.get("https://www.ajio.com/shop/sale"); //Open site
    	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    	//driver.findElement(By.className("ic-close-quickview")).click();
    	
    	//2) Enter Bags in the Search field and Select Bags in Women Handbags
    	
    	String parentWindow = driver.getWindowHandle();
    	
    	driver.findElement(By.name("searchVal")).sendKeys("Bags");
    	driver.findElement(By.xpath("(//span[contains(text(),'Women Handbags')])[1]")).click();
    	
    	//3) Click on five grid and Select SORT BY as "What's New"
    	
    	driver.findElement(By.className("five-grid")).click();
    	WebElement sortBy = driver.findElement(By.xpath("//div[@class='filter-dropdown']/select"));
    	Select select = new Select(sortBy);
    	select.selectByValue("newn");
    	Thread.sleep(3000);
    	//4) Enter Price Range Min as 2000 and Max as 5000
    	
    	driver.findElement(By.xpath("//span[text()='price']")).click();
    	Thread.sleep(3000);
    	driver.findElement(By.id("minPrice")).sendKeys("2000");
    	Thread.sleep(3000);
    	driver.findElement(By.id("maxPrice")).sendKeys("5000");
    	Thread.sleep(3000);
    	driver.findElement(By.xpath("//input[@id='maxPrice']//following-sibling::button")).click();
    	Thread.sleep(3000);
    	
    	
    	//5) Click on the product "Puma Ferrari LS Shoulder Bag"
    	
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Ferrari LS Shoulder Bag')]"))).click();
    	
    	//6) Verify the Coupon code for the price above 2690 is applicable for your product, 
    	//if applicable the get the Coupon Code and Calculate the discount price for the coupon
    	
    	//String cupnCode = driver.findElement(By.xpath("//div[@class='promo-title']/br")).getText();
    	
    	Set<String> handelWindows = driver.getWindowHandles();
		List<String> allWindows = new ArrayList<String>();
		allWindows.addAll(handelWindows);
		driver.switchTo().window(allWindows.get(1));
    	
    	String priceOfBag = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("prod-sp"))).getText();    	    	
    	String rplBag = priceOfBag.replaceAll("\\D", "");
    	int bagPrice = Integer.parseInt(rplBag);
    	System.out.println("Bag price is : "+bagPrice);
    	
    	if (bagPrice>=2690) {
			System.out.println("Coupon code can be applied");
		} else {
			System.out.println("need to purchase aboce 2690 to apply coupon");
		}
    	
    	String discPrice = driver.findElement(By.xpath("//div[@class='promo-discounted-price']/span")).getText();
    	discPrice = discPrice.replaceAll("\\D", "");
    	int discbagPrice = Integer.parseInt(discPrice);
    	//System.out.println("Discounted Price for the bag is : "+discbagPrice);

    	int discTotPrice = bagPrice-discbagPrice;
    	System.out.println("Discount availed is : "+ discTotPrice);
    	
    	//7) Check the availability of the product for pincode 560043, print the expected delivery date if it is available
    	
    	driver.findElement(By.xpath("//span[contains(text(),'Enter pin-code')]")).click();
    	driver.findElement(By.name("pincode")).sendKeys("635001",Keys.ENTER);
    	String expDelivery = driver.findElement(By.className("edd-message-success-details-highlighted")).getText();
    	System.out.println("Expected delivery date is : "+ expDelivery);
    	
    	//8) Click on Other Informations under Product Details and Print the Customer Care address, phone and email
    	
    	driver.findElement(By.className("other-info-toggle")).click();
    	String custDetails = driver.findElement(By.xpath("//span[text()='Customer Care Address']//following-sibling::span[2]")).getText();
    	System.out.println("Customer details is : "+ custDetails);
    	
    	//9) Click on ADD TO BAG and then GO TO BAG
    	
    	driver.findElement(By.xpath("//span[text()='ADD TO BAG']")).click();
    	Thread.sleep(8000);
    	driver.findElement(By.xpath("//span[text()='GO TO BAG']")).click();
    	Thread.sleep(3000);
    	
    	
    	//10) Check the Order Total before apply coupon
    	
    	String cartPrice = driver.findElement(By.xpath("//section[@id='orderTotal']/span[2]")).getText();
    	String rplCart = cartPrice.replaceAll("\\D", "");
    	String substring = rplCart.substring(0, 4);
    	int cartTotPrice = Integer.parseInt(substring);
    	System.out.println("Price of the product before applying coupon :"+cartTotPrice);
    	Thread.sleep(3000);
    	
    	
    	//11) Enter Coupon Code and Click Apply
    	
    	driver.findElement(By.id("couponCodeInput")).sendKeys("EPIC");
    	Thread.sleep(3000);
    	driver.findElement(By.xpath("//button[text()='Apply']")).click();
    	
		//12) Verify the Coupon Savings amount(round off if it in decimal) under Order Summary and the matches the amount calculated in Product details
    	
    	String cupnSaving = driver.findElement(By.xpath("//span[text()='Coupon savings']//following-sibling::span")).getText();
    	String rplSaving = cupnSaving.replaceAll("[\\D]*\\s[\\D]*", "");
    	Double savingAmt = Double.parseDouble(rplSaving);
    	long roundValue = Math.round(savingAmt);
    	
    	System.out.println("Round off discount value is : "+roundValue);
    	
    	if (roundValue==discTotPrice) {
			System.out.println("Discounted Price matches with cart and product details");
		} else {
			System.out.println("Discounted price doesnt matches with cart and product details");
		}
    	
    	//13) Click on Delete and Delete the item from Bag
    	
    	driver.findElement(By.xpath("//div[text()='Delete']")).click();
    	Thread.sleep(3000);
    	driver.findElement(By.xpath("//div[text()='DELETE']")).click();
    	
    	//14) Close all the browsers
    	
    	driver.close();
    	Thread.sleep(3000);
    	driver.switchTo().window(parentWindow).close();
    	
    }
}
