package eu.bitwalker.useragentutils.browser;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;

import static java.util.Arrays.asList;

public class LotusParameterizedTest extends AbstractUserAgentParameterizedTest {

    public LotusParameterizedTest(String userAgentValue,
                                  Browser expectedBrowser, String browserVersionExpected,
                                  OperatingSystem osExpected) {
        super(userAgentValue, expectedBrowser, browserVersionExpected,
                osExpected);
    }

    @Parameters
    public static Collection<Object[]> testData() {
        return asList(new Object[][]{
                // lotusNotes
                {"Mozilla/4.0 (compatible; Lotus-Notes/5.0; Windows-NT)",
                        Browser.LOTUS_NOTES, "5.0", OperatingSystem.WINDOWS},
                {"Mozilla/4.0 (compatible; Lotus-Notes/6.0; Windows-NT)",
                        Browser.LOTUS_NOTES, "6.0", OperatingSystem.WINDOWS}
        });
    }
}
