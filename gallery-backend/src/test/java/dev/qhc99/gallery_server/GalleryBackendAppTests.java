package dev.qhc99.gallery_server;

import dev.qhc99.gallery_server.config.AppProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.net.URI;
import java.net.URL;

@SpringBootTest
@ActiveProfiles("key")
public class GalleryBackendAppTests {
  @Autowired
  private AppProperties appProperties;

  @Test
  public void propertiesRead() {
    var urls = appProperties.getOauth2().getAuthorizedRedirectUris();
    Assert.isTrue(urls.size() != 0, "not empty");
    for (var url : urls) {
      try {
        URI authorizedURI = URI.create(url);
      } catch (Exception e) {
        Assert.isTrue(false, e.toString());
      }
    }
  }

}
