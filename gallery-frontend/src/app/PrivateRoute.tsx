import React, { useEffect, useState } from 'react';
import {
  Navigate,
  Outlet
} from "react-router-dom";


function ProtectedRoute(isAllowed, redirect="/login") {
  useEffect(()=>console.log("into private"))
  return isAllowed ? <Outlet /> : <Navigate to={redirect} replace/>;
};

export default ProtectedRoute