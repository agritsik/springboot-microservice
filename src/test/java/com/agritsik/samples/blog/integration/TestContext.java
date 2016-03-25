package com.agritsik.samples.blog.integration;

import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class TestContext {
    private long createdId = 0;
    private URI createdURL;

    public long getCreatedId() {
        return createdId;
    }

    public void setCreatedId(long createdId) {
        this.createdId = createdId;
    }

    public URI getCreatedURL() {
        return createdURL;
    }

    public void setCreatedURL(URI createdURL) {
        this.createdURL = createdURL;
    }
}
