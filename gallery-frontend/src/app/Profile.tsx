import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import './Profile.css';

function Profile(props) {
  return (
    <div className="profile-container">
      {props.currentUser === null && <Navigate to="/login" replace />}
      <div className="container">
        <div className="profile-info">
          <div className="profile-avatar">
            {

              props.currentUser && (props.currentUser.imageUrl ? (
                <img src={props.currentUser.imageUrl} alt={props.currentUser.name} />
              ) : (
                <div className="text-avatar">
                  <span>{props.currentUser.name && props.currentUser.name[0]}</span>
                </div>
              ))
            }
          </div>
          <div className="profile-name">
            <h2>{props.currentUser && props.currentUser.name}</h2>
            <p className="profile-email">{props.currentUser && props.currentUser.email}</p>
          </div>
        </div>
      </div>
    </div>
  );

}

export default Profile