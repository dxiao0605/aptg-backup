import { persistReducer } from 'redux-persist';
import asyncSessionStorage from 'redux-persist/lib/storage/session';

import rootReducer from './RootReducer';

const rootPersistConfig = {
    key: 'root',
    storage: asyncSessionStorage,
    whilelist: ['loginReducer', 'appReducer', 'alertReducer'],
    blacklist: []
}

export default persistReducer(rootPersistConfig, rootReducer)