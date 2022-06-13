import React, { Component } from 'react';
import {
  Route,
  Routes
} from 'react-router-dom';
import AppHeader from '../common/AppHeader';
import Home from './home/Home';
import Login from './user/Login';
import Signup from './user/Signup';
import Profile from './user/Profile';
import OAuth2RedirectHandler from './user/OAuth2RedirectHandler';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import { getCurrentUser } from '../utils/APIUtils';
import { ACCESS_TOKEN } from '../constants';
import PrivateRoute from '../common/PrivateRoute';
// import 'react-s-alert/dist/s-alert-default.css';
// import 'react-s-alert/dist/s-alert-css-effects/slide.css';
import './App.css';

class App extends Component<any, any> {
  constructor(props) {
    super(props);
    this.state = {
      authenticated: false,
      currentUser: null,
      loading: true
    }
    console.log("debug")

    this.loadCurrentlyLoggedInUser = this.loadCurrentlyLoggedInUser.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
  }

  loadCurrentlyLoggedInUser() {
    getCurrentUser()
      .then(response => {
        this.setState({
          currentUser: response,
          authenticated: true,
          loading: false
        });
      }).catch(error => {
        this.setState({
          loading: false
        });
      });
  }

  handleLogout() {
    localStorage.removeItem(ACCESS_TOKEN);
    this.setState({
      authenticated: false,
      currentUser: null
    });
    // TODO Alert UI
    alert("You're safely logged out!");
  }

  componentDidMount() {
    this.loadCurrentlyLoggedInUser();
  }

  render() {
    if (this.state.loading) {
      return <LoadingIndicator />
    }

    return (
      <div className="app">
        <div className="app-top-box">
          <AppHeader authenticated={this.state.authenticated} onLogout={this.handleLogout} />
        </div>
        <div className="app-body">
          <Routes>
            <div className="app-top-box">
              <AppHeader authenticated={this.state.authenticated} onLogout={this.handleLogout} />
            </div>
            <Route path="/" element={<Home />}></Route>
            <Route path="/profile"
              element={
                (<PrivateRoute eauthenticated={this.state.authenticated} >
                  <Profile currentUser={this.state.currentUser} />
                </PrivateRoute>)
              } />

            <Route path="/login"
              element={<Login authenticated={this.state.authenticated} />}></Route>
            <Route path="/signup"
              element={<Signup authenticated={this.state.authenticated} />}></Route>
            <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />}></Route>
            <Route element={<NotFound />}></Route>
          </Routes>
        </div>
        // TODO alert UI
        {/* <Alert stack={{limit: 3}} 
          timeout = {3000}
          position='top-right' effect='slide' offset={65} /> */}
      </div>
    );
  }
}

export default App;
