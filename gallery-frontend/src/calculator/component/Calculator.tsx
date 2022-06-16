import React, { useState } from "react";
import Display from "./Display";
import ButtonPanel from "./ButtonPanel";
import calculate from "../logic/calculate";
import "./Calculator.css";

export default function Calculator(){
  const init_state: any = {
    total: null,
    next: null,
    operation: null,
  };
  let [state, setState] = useState(init_state);

  const handleClick = buttonName => {
    const updated = calculate(state, buttonName);
    setState({...state, ...updated});
  };
  
  return (
    <div className="component-app">
      <Display value={state.next || state.total || "0"} />
      <ButtonPanel clickHandler={handleClick} />
    </div>
  );
}
