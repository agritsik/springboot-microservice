package com.agritsik.samples.blog;

import com.agritsik.samples.blog.boundary.PostRestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, MockedConfiguration.class})
@ActiveProfiles("mock")
public class SpringProfilesTest {

    @Autowired
    PostRestClient postRestClient;

    @Test
    public void testDefaultProfile() throws Exception {
        assertTrue(new MockUtil().isMock(postRestClient));
    }

}
