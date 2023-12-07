import { createStore, applyMiddleware } from 'redux';
import { persistStore } from 'redux-persist';
import createSagaMiddleware from 'redux-saga';
import logger from 'redux-logger';
// import rootReducer from '../reducers/RootReducer';
import rootPersistReducer from '../reducers/PersistReducer';
import rootSaga from './rootSaga';

// Log only in development
// create the saga middleware
// console.log('process.env.NODE_ENV',process.env.NODE_ENV)
const sagaMiddleware = createSagaMiddleware();
let middleware = [sagaMiddleware];

if (process.env.NODE_ENV !== 'production'){
    middleware = [...middleware, logger];
}
// mount it on the Store
const store = createStore(
    rootPersistReducer,
    applyMiddleware(...middleware),
);

// then run the saga
sagaMiddleware.run(rootSaga);

const persistor = persistStore(store);

export { store, persistor };