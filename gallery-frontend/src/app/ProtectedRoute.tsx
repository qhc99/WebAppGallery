import React, { useEffect, useState } from 'react';
import {
  Navigate,
  Outlet
} from "react-router-dom";


function ProtectedRoute(isAllowed, redirect = "/login") {
  if(isAllowed === true){
    return <Outlet />;
  }
  return <Navigate to={redirect} replace />;
};

export default ProtectedRoute