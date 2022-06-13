import React from 'react';
import {
    Route,
    Navigate
  } from "react-router-dom";
  
  
function PrivateRoute (authenticated, children )  {
  if(!authenticated){
    return  <Navigate
    to='/login'
    replace={true}
  />;
  }
  else return children;
};
  
export default PrivateRoute