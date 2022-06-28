package dev.qhc99.gallery_server.data_class;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUserJson {
  String login;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }
}
