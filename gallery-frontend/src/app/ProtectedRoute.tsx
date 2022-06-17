import React, { useEffect, useState } from 'react';
import {
  Navigate,
  Outlet
} from "react-router-dom";


function ProtectedRoute(props) {
  useEffect(()=>{
    if(props.warning != null && !!!props.isAllowed){
      alert(props.warning)
    }
  })
  return <div>
    {props.isAllowed &&  <Outlet />}
    {!!!props.isAllowed && <Navigate to={props.redirect} replace />}
  </div>;
};

export default ProtectedRoute