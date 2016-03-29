package spring;

import com.agritsik.samples.blog.Application;
import com.agritsik.samples.blog.MockedConfiguration;
import com.agritsik.samples.blog.boundary.PostRestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, MockedConfiguration.class})
@ActiveProfiles("default")
public class SpringDefaultProfilesTest {

    @Autowired
    PostRestClient postRestClient;

    @Test
    public void testDefaultProfile() throws Exception {
        assertFalse(new MockUtil().isMock(postRestClient));
    }

}
