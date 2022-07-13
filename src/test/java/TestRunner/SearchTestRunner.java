package TestRunner;

import org.testng.annotations.Test;
import page.SearchPage;
import setup.Setup;

public class SearchTestRunner extends Setup {
    SearchPage searchPage;

    @Test
    public void searchResultVerification() throws InterruptedException {
        searchPage = new SearchPage(driver);
        searchPage.searching();
        searchPage.traversingPages();
    }
}
