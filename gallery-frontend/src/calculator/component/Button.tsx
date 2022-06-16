import React, { useEffect, useState } from "react";
import "./Button.css";

export default function Button(props){
  const classNameInit = [
    "component-button",
    props.orange ? "orange" : "",
    props.wide ? "wide" : "",
  ];


  const handleClick = () => {
    props.clickHandler(props.name);
  };


  return (
    <div className={classNameInit.join(" ").trim()}>
      <button onClick={handleClick}>{props.name}</button>
    </div>
  );
}