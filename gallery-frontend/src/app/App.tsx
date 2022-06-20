import React, { Component } from 'react';
import {
  BrowserRouter,
  Route,
  Routes,
} from 'react-router-dom';
import AppHeader from './AppHeader';
import Home from './Home';
import Login from './Login';
import Signup from './Signup';
import Profile from './Profile';
import OAuth2RedirectHandler from './OAuth2Redirect';
import NotFound from './NotFound';
import { getCurrentUser, getUserStaredStatus } from '../utils/APIUtils';
import { ACCESS_TOKEN } from '../constants';
import ProtectedRoute from './ProtectedRoute';
import { useState } from 'react';
import { useEffect } from 'react';
import Calculator from '../calculator/component/Calculator'


function App(props) {


  const [user, setUser] = useState({});
  const [appAuthenticated, setAppAuthenticated] = useState(false)
  const [userStared, setUserStared] = useState(false)
  // const [loading, setLoading] = useState(true);


  function handleLogout() {
    localStorage.removeItem(ACCESS_TOKEN);
    setUser({});
    setAppAuthenticated(false)
    // TODO Alert UI
    alert("You're safely logged out!");
  }
  // double effect

  useEffect(() => {
    getCurrentUser()
      .then(response => {
        setUser(response);
        setAppAuthenticated(true)
      }).catch((_) => {
      });
  }, [])

  useEffect(() => {
    getUserStaredStatus()
      .then(response => {
        setUserStared(response.success === true)
      }).catch(e => {
        console.log("user not stared.")
        console.log(e.error)
      })
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
              <Route element={<ProtectedRoute isAllowed={appAuthenticated} redirect="/login" />} >
                <Route path='/profile' element={<Profile currentUser={user} />} />
              </Route>
              <Route element={<ProtectedRoute isAllowed={userStared} redirect="/" warning="please login and star my project at github" />} >
                <Route path='/calculator' element={<Calculator />} />
              </Route>

              <Route path='*' element={<NotFound />} />
            </Route>

          </Routes>
        </BrowserRouter>
      </div>
      {/* {loading && <LoadingIndicator />} */}
      {/* // TODO alert UI */}
      {/* <Alert stack={{limit: 3}} 
          timeout = {3000}
          position='top-right' effect='slide' offset={65} /> */}
    </div>
  );

}

export default App;
