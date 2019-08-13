import React from "react";
import { StateProvider } from './simplechat/state';
import reducer, {initialStates} from './store/reducers';
import ChatApp from './components/ChatApp';

const App = () => {
    return (
        <StateProvider initialState={initialStates} reducer={reducer}>
           <ChatApp />
        </StateProvider>
    );
}

export default App;