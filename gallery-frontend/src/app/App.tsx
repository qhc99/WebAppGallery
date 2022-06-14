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


  const [user, setUser] = useState(null);
  const [appAuthenticated, setAppAuthenticated] = useState(false)
  const [loading, setLoading] = useState(true);


  function handleLogout() {
    localStorage.removeItem(ACCESS_TOKEN);
    setUser(null);
    setAppAuthenticated(false)
    // TODO Alert UI
    alert("You're safely logged out!");
  }

  useEffect(() => {
    getCurrentUser()
      .then(response => {
        setUser(response);
        setAppAuthenticated(true)
        setLoading(false)
      }).catch((_) => {
        setLoading(false)
      });
  }, [appAuthenticated])



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
                  <AppHeader authenticated={appAuthenticated} onLogout={handleLogout} />
                </div>
              }>
              <Route index element={<Home />} />
              <Route path="/profile"
                element={<PrivateRoute authenticated={appAuthenticated} />} >
                <Route path='/profile' element={
                  <Profile currentUser={user} />} />
              </Route>
              <Route path="/login"
                element={<Login authenticated={appAuthenticated} setAppAuthed={setAppAuthenticated} />} />
              <Route path="/signup"
                element={<Signup authenticated={appAuthenticated} />} />
              <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler changeAuth={setAppAuthenticated} />} />
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
