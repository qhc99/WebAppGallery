package dev.qhc99.gallery_server.data_class;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubUserJson {
  String login;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GithubUserJson that = (GithubUserJson) o;
    return login.equals(that.login);
  }

  @Override
  public int hashCode() {
    return Objects.hash(login);
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  @Override
  public String toString() {
    return String.format("GithubUserJson{login: %s}", login);
  }
}
