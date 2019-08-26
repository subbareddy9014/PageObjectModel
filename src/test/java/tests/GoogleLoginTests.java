package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utility.BaseTest;
import utility.CredentialsReader;

public class GoogleLoginTests extends BaseTest {

    @Test(dataProvider = "credentials", dataProviderClass = CredentialsReader.class)
    public void loginTest(String userName, String password) {
        Assert.assertTrue(googleUserNameEntryPage.enterUserName(userName),"User name is wrong");
        Assert.assertTrue(googlePasswordEntryPage.enterPassword(password), "Password is wrong");
        Assert.assertTrue(googleAccountHomePage.validateLogin(), "Login is not successful");
        googleAccountHomePage.signOut();
    }
}
