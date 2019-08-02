import React from "react";
import { StateProvider } from './simplechat/state';
import reducer from './store/reducers';
import Chat from './components/Chat';

const App = () => {
    return (
        <StateProvider initialState={{}} reducer={reducer}>
           <Chat />
        </StateProvider>
    );
}

export default App;