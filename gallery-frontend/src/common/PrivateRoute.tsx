import React, { useState } from 'react';
import {
  Route,
  Navigate,
  Outlet
} from "react-router-dom";


function PrivateRoute(authenticated) {
  return authenticated ? <Outlet /> : <Navigate to="/login" />;
};

export default PrivateRoute