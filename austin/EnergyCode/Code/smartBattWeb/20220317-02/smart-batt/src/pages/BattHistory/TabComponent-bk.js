import React, { useLayoutEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { checkUnitStepSize } from './utils';
import SwipeableViews from 'react-swipeable-views';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import {AntTabs,AntTab} from '../../components/CusTabStyle';
import { useTranslation } from 'react-i18next'
import HistoryChartHeader from '../../components/Chart/HistoryChartHeader'; //圖表抬頭
import CusTabPanel from '../../components/CusTabPanel'; //頁籤內容
import CusChart from './CusChart'; // 各曲線圖
import IMPTypeUnit from './IMPTypeUnit';



function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    'aria-controls': `full-width-tabpanel-${index}`,
  };
}

const useStyles = makeStyles((theme) => ({
  root: {
    marginBottom: theme.spacing(2),
    backgroundColor: theme.palette.background.paper,
  },
}));

const TabComponent = ({
    chart,IRChart,VolChart,TemperatureChart,RecTime,
    MinVol,MaxVol,stepSizeVol,
    MinIR,MaxIR,stepSizeIR,
    MaxTemperature,MinTemperature,stepSizeTemperature,
    IMPType,handleCSVEvent,handleExcelEvent,chartErrorMessage }) => {
  const { t } = useTranslation();
  const classes = useStyles();
  const theme = useTheme();
  const RecordTimeData = useSelector( (state => state.BattFilterReducer.isFilterRecordTimeData))
  const unitStepSize = checkUnitStepSize({item:RecordTimeData, type: RecordTimeData.Radio});
  const [value, setValue] = React.useState(0);
  const [width,height] = useWindowSize();

  // 變更頁籤
  const handleChange = (event, newValue) => {
    setValue(newValue);
  };
  // 變更頁籤
  const handleChangeIndex = (index) => {
    setValue(index);
  };

  // 列印圖表
  const handleChartPrint = async() => {
    await window.print()
  }

  function useWindowSize() {
    const [size, setSize] = useState([0, 0]);
    useLayoutEffect(() => {
      function updateSize() {
        setSize([window.innerWidth, window.innerHeight]);
      }
      window.addEventListener('resize', updateSize);
      updateSize();
      return () => window.removeEventListener('resize', updateSize);
    }, []);
    return size;
  }

  return (
    <div className={classes.root}>
      <SwipeableViews
        axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
        index={value}
        onChangeIndex={handleChangeIndex}
      >
        <CusTabPanel value={value} index={0} dir={theme.direction}>
          <div className="col-12 p-0">{/* 內阻曲線 */}
            <div className="card border-0 p-2">
                <h4 className="text-center print-only">{t('1601')} </h4>
                <div className="print-hide">
                  <HistoryChartHeader title={t('1601')} handleExcelEvent={()=>{handleExcelEvent()}} handleCSVEvent={()=>{handleCSVEvent()}} handlePrintEvent={()=>{handleChartPrint()}} />
                </div>
                <div className="chart_content historyChart">
                    {   //判斷是否有電池狀態
                      (()=>{
                        if(chart === true && IRChart && IRChart.length > 0) {
                          return (
                            <div>
                            {/* 內組名稱 */}
                              <IMPTypeUnit IMPType={IMPType} />
                              <CusChart data={IRChart} labels={RecTime} maxValue={MaxIR} minValue={MinIR} stepSize={stepSizeIR} unitStepSize={unitStepSize} windowSize={width} />
                            </div>
                          )}else {
                            return <div className="text-center p-4">{chartErrorMessage}</div>
                          }
                        }
                      )()
                    }
                </div>
            </div>
        </div>
        </CusTabPanel>
        <CusTabPanel value={value} index={1} dir={theme.direction}>
        <div className="col-12 p-0">{/* 電壓曲線 */}
          <div className="card border-0 pl-2 pr-2">
              <h4 className="text-center w-100 print-only">{t('1602')} </h4>
              <div className="print-hide">
                <HistoryChartHeader title={t('1602')}  handleExcelEvent={()=>{handleExcelEvent()}} handlePrintEvent={()=>{handleChartPrint()}}/>
              </div> 
              <div className="chart_content historyChart">
                  {   //判斷是否有電壓
                    (chart === true && VolChart && VolChart.length > 0) ?
                      <div>
                        <div className='unit'>[V]</div>
                        <CusChart data={VolChart} labels={RecTime} maxValue={MaxVol} minValue={MinVol} stepSize={stepSizeVol} unitStepSize={unitStepSize} showFloatValue={true}  windowSize={width} />
                      </div>
                    : <div className="text-center p-4">{chartErrorMessage}</div>
                  }
              </div>
          </div>
        </div>
        </CusTabPanel>
        <CusTabPanel value={value} index={2} dir={theme.direction}>
        <div className="col-12 p-0">{/* 溫度曲線 */}
            <div className="card border-0 pl-2 pr-2">
                <h4 className="text-center w-100 print-only">{t('1603')} </h4>
                <div className="print-hide">
                  <HistoryChartHeader title={t('1603')}  handleCSVEvent={()=>{handleCSVEvent()}} handleExcelEvent={()=>{handleExcelEvent()}} handlePrintEvent={()=>{handleChartPrint()}}/>
                </div>
                <div className="chart_content historyChart">
                  {   //判斷是否有溫度
                    (chart === true && TemperatureChart && TemperatureChart.length > 0) ?
                      <div>
                        <div className='unit'>[℃]</div>
                        <CusChart data={TemperatureChart} labels={RecTime} maxValue={MaxTemperature} minValue={MinTemperature} stepSize={stepSizeTemperature} unitStepSize={unitStepSize} windowSize={width} />
                      </div>
                    : <div className="text-center p-4">{chartErrorMessage}</div>
                  }
              </div>
            </div>
        </div>
        </CusTabPanel>
      </SwipeableViews>
    
    {/* 頁籤 */}
    <div className="print-hide">
    <AppBar position="static" color="default">
        <AntTabs
          value={value}
          onChange={handleChange}
          variant="fullWidth"
          aria-label="full width tabs example"
        >
          <AntTab label={t('1039')} {...a11yProps(0)} />{/* 內阻 */}
          <AntTab label={t('1040')} {...a11yProps(1)} />{/* 電壓 */}
          <AntTab label={t('1041')} {...a11yProps(2)} />{/* 溫度 */}
        </AntTabs>
      </AppBar>
      </div>
    </div>
  );
}

TabComponent.defaultProps = {
  MinVol:0,
  MaxVol:0,
  stepSizeVol:0.2,
  MinIR:0,
  MaxIR:0,
  stepSizeIR:1,
  MaxTemperature:0,
  MinTemperature:0,
  stepSizeTemperature:1,
  handleCSVEvent:()=>{},
}
export default TabComponent;