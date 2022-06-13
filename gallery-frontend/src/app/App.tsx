import React, { Component } from 'react';
import {
  BrowserRouter,
  Route,
  Routes,
  useLocation
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
import { useState } from 'react';
import { useEffect } from 'react';


function App(props) {

  const nullUserAuth = {
    authenticated: false,
    currentUser: null,
  }

  const [userAuth, setUserAuth] = useState(nullUserAuth);
  const [loading, setLoading] = useState(true);


  function handleLogout() {
    localStorage.removeItem(ACCESS_TOKEN);
    setUserAuth(nullUserAuth);
    // TODO Alert UI
    alert("You're safely logged out!");
  }

  useEffect(() => {
    getCurrentUser()
      .then(response => {
        setUserAuth({
          currentUser: response,
          authenticated: true,
        });
        setLoading(false)
      }).catch((_) => {
        setLoading(false)
      });
  }, [])



  if (loading) {
    return <LoadingIndicator />
  }

  return (
    <div className="app">

      <div className="app-body">
        <BrowserRouter>
          <Routes>
            <Route path='/'
              element={
                <div className="app-top-box">
                  <AppHeader authenticated={userAuth.authenticated} onLogout={handleLogout} />
                </div>
              }>
              <Route index element={<Home />} />
              <Route path="/profile"
                element={
                  (<PrivateRoute eauthenticated={userAuth.authenticated} >
                    <Profile currentUser={userAuth.currentUser} />
                  </PrivateRoute>)
                } />
              <Route path="/login"
                element={<Login authenticated={userAuth.authenticated} />} />
              <Route path="/signup"
                element={<Signup authenticated={userAuth.authenticated} />} />
              <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
              <Route element={<NotFound />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </div>
      {/* // TODO alert UI */}
      {/* <Alert stack={{limit: 3}} 
          timeout = {3000}
          position='top-right' effect='slide' offset={65} /> */}
    </div>
  );

}

export default App;
