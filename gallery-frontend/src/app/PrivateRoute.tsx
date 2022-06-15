import React, { useEffect, useState } from 'react';
import {
  Navigate,
  Outlet
} from "react-router-dom";


function ProtectedRoute(isAllowed, redirect = "/login") {
  useEffect(()=>{
    console.log(new Date() + ": "+ isAllowed)
  },[])
  return (<div>
    {isAllowed !==true && <Navigate to={redirect} replace />}
    <Outlet />
  </div>);
};

export default ProtectedRoute