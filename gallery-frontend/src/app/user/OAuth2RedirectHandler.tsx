import React, { Component, useEffect, useState } from 'react';
import { ACCESS_TOKEN } from '../../constants';
import { Navigate, useLocation } from 'react-router-dom'
import { TransitionState } from '../../TransitionState';

function OAuth2RedirectHandler(props) {
  let location = useLocation();
  const [transition, setTransition] = useState(TransitionState.Loading);
  useEffect(() => {
    function getUrlParameter(name) {
      name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
      var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

      var results = regex.exec(location.search);
      return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };
    const token = getUrlParameter('token');
    const error = getUrlParameter('error');
    localStorage.setItem(ACCESS_TOKEN, token);
    const ns = error === '' ? TransitionState.Success : TransitionState.Fail;
    setTransition(ns)
    props.setAppAuthed(error === '')
  }, [props, location.search]);



  return (<div>
    {(transition === TransitionState.Fail) && <Navigate to="/login" />}
    {(transition === TransitionState.Success) && <Navigate to="/" />}
  </div>);


}

export default OAuth2RedirectHandler;