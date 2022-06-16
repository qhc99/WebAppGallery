import React from "react";

import "./Display.css";

export default function Display(props){
  return (
    <div className="component-display">
      <div>{props.value}</div>
    </div>
  );
}
