import React from "react";
import { StateProvider } from './simplechat/state';
import reducer, {initialStates} from './store/reducers';
import Chat from './components/Chat';

const App = () => {
    return (
        <StateProvider initialState={initialStates} reducer={reducer}>
           <Chat />
        </StateProvider>
    );
}

export default App;