import React, { Component } from 'react';
import {
  BrowserRouter,
  Route,
  Routes,
  useLocation
} from 'react-router-dom';
import AppHeader from './AppHeader';
import Home from './Home';
import Login from './Login';
import Signup from './Signup';
import Profile from './Profile';
import OAuth2RedirectHandler from './OAuth2Redirect';
import NotFound from './NotFound';
import LoadingIndicator from './LoadingIndicator';
import { getCurrentUser } from '../utils/APIUtils';
import { ACCESS_TOKEN } from '../constants';
import ProtectedRoute from './PrivateRoute';
// import 'react-s-alert/dist/s-alert-default.css';
// import 'react-s-alert/dist/s-alert-css-effects/slide.css';
// import './App.css';
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
    console.log("load user" + new Date())
    setLoading(true);
    getCurrentUser()
      .then(response => {
        setUser(response);
        setAppAuthenticated(true)
        setLoading(false)
      }).catch((_) => {
        setLoading(false)
      });
  }, [appAuthenticated])

  return (

    <div className="app">
      <div className="app-body">
        <BrowserRouter>
          <Routes>
            <Route path='/'
              element={
                <AppHeader authenticated={appAuthenticated} onLogout={handleLogout} />
              }>
              <Route index element={<Home />} />
              <Route path="/login"
                element={<Login authenticated={appAuthenticated} setAppAuthed={setAppAuthenticated} />} />
              <Route path="/signup"
                element={<Signup authenticated={appAuthenticated} />} />
              <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler setAppAuthed={setAppAuthenticated} />} />
              <Route element={<ProtectedRoute isAllowed={appAuthenticated} />} >
                <Route path='/profile' element={<Profile currentUser={user} />} />
              </Route>
              <Route path='*' element={<NotFound />} />
            </Route>

          </Routes>
        </BrowserRouter>
      </div>
      {loading && <LoadingIndicator />}
      {/* // TODO alert UI */}
      {/* <Alert stack={{limit: 3}} 
          timeout = {3000}
          position='top-right' effect='slide' offset={65} /> */}
    </div>
  );

}

export default App;
