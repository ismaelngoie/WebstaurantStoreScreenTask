package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class SearchPage {
    WebDriver driver;
    WebDriverWait wait;
    int totalProducts = 0;
    int productTitleMatched = 0;
    int productTitleNotMatched = 0;

    @FindBy(id = "searchval")
    WebElement searchField;
    @FindBy(xpath = "//button[contains(text(),'Search')]")
    WebElement searchButton;
    @FindBy(xpath = "//a[@data-testid='itemDescription']")
    List<WebElement> productTitle;
    @FindBy(xpath = "//input[@name='addToCartButton']")
    List<WebElement> addToCartButton;
    @FindBy(xpath = "//button[@aria-label='Submit Feedback']")
    List<WebElement> confirmingAddToCartButton;
    @FindBy(xpath = "//span[@class='hidden xsl:inline']")
    WebElement viewCart;
    @FindBy(xpath = "//button[@class='deleteCartItemButton itemDelete__link itemDelete--positioning']")
    WebElement removingCartItem;
    @FindBy(xpath = "//p[@class='header-1']")
    WebElement emptyCartText;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Search method for searching "stainless work table"
    public void searching() {
        searchField.sendKeys("stainless work table");
        searchButton.click();
    }

    //Method for matching the product title with the word "table"
    public void matchingWithProductTitle() {
        int xpathCount = productTitle.size(); //total amount of products in a page
        totalProducts = totalProducts + xpathCount; //counting all the products in all pages
        for (int i = 0; i < xpathCount; i++) { //finding how many products match with the word "title" and how many not match
            String title = productTitle.get(i).getText();
            if (title.contains("Table")) {
                productTitleMatched++;
            } else {
                productTitleNotMatched++;
            }

        }
    }

    //method for moving one page to the next page
    public void traversingPages() throws InterruptedException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        for (int p = 2; p <= 100; p++) {
            matchingWithProductTitle();
            boolean pageExist = driver.findElements(By.xpath("//a[@aria-label='Page " + p + "']")).size() > 0; //checking if the page is exist
            if (pageExist == true) { //if the next page is exist, it will go to that page
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@aria-label='Page " + p + "']")));
                driver.findElement(By.xpath("//a[@aria-label='Page " + p + "']")).click();
            } else {
                break;
            }
        }
        System.out.println("Among " + totalProducts + " products, " + productTitleMatched + " product title matched with the word \'Table\'");
        System.out.println("Among " + totalProducts + " products, " + productTitleNotMatched + " product title not matched with the word \'Table\'");

        //when cannot find next page, the last product will be added to the cart and be removed
        int allAddToCart = addToCartButton.size();
        addToCartButton.get(allAddToCart - 1).click();
        Thread.sleep(3000);
        confirmingAddToCartButton.get(1).click();
        Thread.sleep(12000);
        viewCart.click();
        Thread.sleep(5000);
        removingCartItem.click();
        String text = emptyCartText.getText();
        Assert.assertTrue(text.contains("Your cart is empty."));
    }
}
