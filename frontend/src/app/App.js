import React from "react";
import { StateProvider } from './simplechat/state';
import reducer, {initialStates} from './store/reducers';
import ChatApp from './components/ChatApp';

const App = (props) => {
    return (
        <StateProvider initialState={initialStates} reducer={reducer}>
           <ChatApp auth={props.auth} />
        </StateProvider>
    );
}

export default App;