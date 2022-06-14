import React, { Component, useEffect, useState } from 'react';
import './Login.css';
import { GITHUB_AUTH_URL, ACCESS_TOKEN } from '../../constants';
import { login } from '../../utils/APIUtils';
import { Link, Navigate, useLocation } from 'react-router-dom'
const githubLogo = require('../../img/github-logo.png');

function Login(props) {
  let location = useLocation();
  const [error, setError] = useState(false);
  useEffect(() => {
    // If the OAuth2 login encounters an error, the user is redirected to the /login page with an error.
    // Here we display the error and then remove the error query parameter from the location.
    if (location.state && (location.state as any).error) {
      setTimeout(() => {
        // Alert.error(this.props.location.state.error, {
        //     timeout: 5000
        // });
        alert((location.state as any).error);
        setError(true);
      }, 100);
    }
  },[location])

  if (props.authenticated) {
    return <Navigate to="/" />;
  }

  return (
    <div className="login-container">
      <div className="login-content">
        {error && <Navigate to={"/login"} />}
        <h1 className="login-title">Login to SpringSocial</h1>
        <SocialLogin />
        <div className="or-separator">
          <span className="or-text">OR</span>
        </div>
        <LoginForm {...props} />
        <span className="signup-link">New user? <Link to="/signup">Sign up!</Link></span>
      </div>
    </div>
  );
}

function SocialLogin(props) {
  return (
    <div className="social-login">
      <a className="btn btn-block social-btn github" href={GITHUB_AUTH_URL}>
        <img src={githubLogo} alt="Github" /> Log in with Github</a>
    </div>
  );
}


function LoginForm(props) {
  const nullEmailPasswd = {
    email: '',
    password: ''
  };
  const [emailPasswd, setEmailPasswd] = useState(nullEmailPasswd);
  const [loginSuccessRedirect, setLoginSeccessRedirect] = useState(false);


  const handleInputChange = (event)=> {
    const target = event.target;
    const inputName = target.name;
    const inputValue = target.value;
    setEmailPasswd({...emailPasswd, [inputName]:inputValue});
  }

  const handleSubmit = (event)=> {
    event.preventDefault();

    const loginRequest = emailPasswd;

    login(loginRequest)
      .then(response => {
        localStorage.setItem(ACCESS_TOKEN, response.accessToken);
        alert("You're successfully logged in!");
        setLoginSeccessRedirect(true);
        props.setAppAuthed(true)
      }).catch(error => {
        // Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        alert(error.message);
      });
  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div className="form-item">
          <input type="text" name="email"
            className="form-control" placeholder="Email"
            value={emailPasswd.email} onChange={handleInputChange} required />
        </div>
        <div className="form-item">
          <input type="password" name="password"
            className="form-control" placeholder="Password"
            value={emailPasswd.password} onChange={handleInputChange} required />
        </div>
        <div className="form-item">
          <button type="submit" className="btn btn-block btn-primary">Login</button>
        </div>
      </form>
      {loginSuccessRedirect && <Navigate to={"/"} />}
    </div>
  );
}


export default Login
