import loadable from '@loadable/component';

// Page
export const App = loadable(() => import('../App'));
export const Login = loadable(() => import('../pages/Login'));
export const Home = loadable(() => import('../pages/Home'));
export const AlertUnsolved = loadable(() => import('../pages/AlertUnsolved'));
export const AlertSolved = loadable(() => import('../pages/AlertSolved'));
export const AlertCondition = loadable(() => import('../pages/AlertCondition'));
export const BattGroup = loadable(() => import('../pages/BattGroup'));
export const BattData = loadable(() => import('../pages/BattData'));
export const BattHistory = loadable(() => import('../pages/BattHistory'));
export const BattHistoryManage = loadable(() => import('../pages/BattHistoryManage'));
export const BattManage = loadable(() => import('../pages/BattManage'));
export const GroupManage = loadable(() => import('../pages/GroupManage'));
export const NBManage = loadable(() => import('../pages/NBManage'));
export const Command = loadable(() => import('../pages/Command'));
export const UserManage = loadable(() => import('../pages/UserManage'));
export const SystemSettings = loadable(() => import('../pages/SystemSettings'));

export const PrivacyPolicy = loadable( () => import('../components/Footer/PrivacyPolicy'));