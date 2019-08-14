import React from "react";
import ReactDOM from "react-dom";
import App from "./app/App";

const container = document.querySelector('#simplechat');
const auth = container.getAttribute('auth');

ReactDOM.render(<App auth={auth} />, container);