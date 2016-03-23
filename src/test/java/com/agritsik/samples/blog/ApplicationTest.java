package com.agritsik.samples.blog;

import org.junit.Test;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ApplicationTest {

    @Test
    public void testMain() throws Exception {


        // arrange
        SpringApplication application = spy(SpringApplication.class);

        // act
        Application.main(new String[0]);

        // assert
         verify(application).run();


    }


}