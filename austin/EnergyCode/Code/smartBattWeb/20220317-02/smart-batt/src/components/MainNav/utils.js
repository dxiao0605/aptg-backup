export const getActive = () => {
    // 判斷目前的頁面位置
    const activeLink = window.location.hash;
    const linkToAlert = ['#/AlertUnsolved','#/AlertSolved','#/AlertCondition'];
    const linkToBattData = ['#/BattGroup','#/BattData'];
    const linkToBattManage = ['#/BattManage','#/GroupManage','#/NBManage'];
    const linkToBattHistory = ['#/BattHistory','#/BattHistoryManage'];

    if (activeLink === '#/') {                   //總覽
        return 1200
    }
    else if (linkToAlert.includes(activeLink)) {  //告警
        return 1300
    }
    else if (linkToBattData.includes(activeLink)) {  //電池數據
        return 1400
    }
    else if (linkToBattManage.includes(activeLink)) {    //電池管理
        return 1500
    }
    else if (linkToBattHistory.includes(activeLink)) {    //電池歷史
        return 1600
    }
    else {                                       //總覽
        return 1200
    }
}