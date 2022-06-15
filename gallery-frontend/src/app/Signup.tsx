
import React, { Component, useState } from 'react';
import './Signup.css';
import { Link, Navigate } from 'react-router-dom'
import { GITHUB_AUTH_URL } from '../constants';
import { signup } from '../utils/APIUtils';
const githubLogo = require('../img/github-logo.png');

function Signup(props) {

  if (props.authenticated) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className="signup-container" >
      <div className="signup-content">
        <h1 className="signup-title">Signup with SpringSocial</h1>
        <SocialSignup />
        <div className="or-separator">
          <span className="or-text">OR</span>
        </div>
        <SignupForm {...props} />
        <span className="login-link">Already have an account? <Link to="/login">Login!</Link></span>
      </div>
    </div>
  );
}



function SocialSignup(props) {
  return (
    <div className="social-signup">
      <a className="btn btn-block social-btn github" href={GITHUB_AUTH_URL}>
        <img src={githubLogo} alt="Github" /> Sign up with Github</a>
    </div>
  );
}

function SignupForm(props) {

  const initState = {
    name:  '',
    email: '',
    password: '',
  }

  const [registerInfo, setRegisterInfo] = useState(initState);
  const [registerSuccessRedirect, setRegisterSuccessRedirect] = useState(false);


  const handleInputChange = (event) => {
    const target = event.target;
    const inputName = target.name;
    const inputValue = target.value;
    setRegisterInfo({...registerInfo,[inputName]: inputValue});
  }

  const handleSubmit = (event) => {
    event.preventDefault();

    const signUpRequest = Object.assign({}, registerInfo);

    signup(signUpRequest)
      .then(response => {
        // TODO alert UI
        alert("You're successfully registered. Please login to continue!");
        setRegisterSuccessRedirect(true);
      }).catch(e => {
        // TODO alert UI
        alert((e && e.error) || 'Oops! Something went wrong. Please try again!');
      });
  }


  return (
    <form onSubmit={handleSubmit}>
      {registerSuccessRedirect && <Navigate to={"/login"} replace/>}
      <div className="form-item">
        <input type="text" name="name"
          className="form-control" placeholder="Name"
          value={registerInfo.name} onChange={handleInputChange} required />
      </div>
      <div className="form-item">
        <input type="email" name="email"
          className="form-control" placeholder="Email"
          value={registerInfo.email} onChange={handleInputChange} required />
      </div>
      <div className="form-item">
        <input type="password" name="password"
          className="form-control" placeholder="Password"
          value={registerInfo.password} onChange={handleInputChange} required />
      </div>
      <div className="form-item">
        <button type="submit" className="btn btn-block btn-primary" >Sign Up</button>
      </div>
    </form>

  );

}

export default Signup