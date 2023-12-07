import React, { useState, useRef, Fragment, useEffect } from "react";
import { useSelector } from "react-redux";
// We will use these things from the lib
// https://react-google-maps-api-docs.netlify.com/
import {
  useLoadScript,
  GoogleMap,
} from "@react-google-maps/api";
import { useTranslation } from 'react-i18next';
import Map from './Map';
import { CardSpinner } from '../Spinner';


function WrappedMap({ data,IMPType, mapCenter, dfZoom, mapHeight }) {
  const { t } = useTranslation();
  const ApiKey = useSelector( state => state.LoginReducer.Key);
  // The things we need to track in state
  const mapRef = useRef(null);  //實體化map  
  const [selectedPlace, setSelectedPlace] = useState(null); //目前點選的marker
  const [markerMap, setMarkerMap] = useState(); // 實體化map上marker
  const [center, setCenter] = useState(mapCenter); //設定地圖中心點
  const [zoom, setZoom] = useState(dfZoom);  //比例尺
  const [infoOpen, setInfoOpen] = useState(false);  //是否打開infoWindow
  // 地圖電池資訊(zoom<11 false 顯示群組 | zoom > 11 true 單點+顯示資訊)
  const [markerVisible, setMarkerVisible] = useState(true);  //判斷是否顯示 單點資訊 

  // 生命週期
  useEffect(() => {
    if (!mapRef.current) return;
    //判斷是否顯示單點資訊
    if (zoom > 11) {
      // 單顆
      setMarkerVisible(true)
    } else {
      // 群組
      setMarkerVisible(false)
      setInfoOpen(false);
    }
  }, [setMarkerVisible, setInfoOpen, setZoom, zoom])




  // Load the Google maps scripts
  const { isLoaded } = useLoadScript({
    // Enter your own Google Maps API key
    googleMapsApiKey: ApiKey,
  });


  // 載入座標點&實體化map
  const loadHandler = map => {
    if (map === undefined) { return; }
    // Store a reference to the google map instance in state
    mapRef.current = map;
    // 第一次載入時判斷顯示Marker樣式
    if (zoom > 11) {
      // 單顆
      setMarkerVisible(true)
    } else {
      // 群組
      setMarkerVisible(false)
      setInfoOpen(false);
    }
  };

  // 變更中心點
  const handleCenterChanged = (newPos) => {  
    if (newPos !== undefined) {
      setCenter({
        lat: parseFloat(newPos.lat),
        lng: parseFloat(newPos.lng)
      });
    }
  }

  // 變更比例尺
  const handleZoomChanged = () => {
    if (!mapRef.current) return;
    const newZoom = mapRef.current.getZoom();
    setZoom(newZoom);
  }
  // 還原比例尺 zoom 11
  const handleResetZoom = () => {
    if (!mapRef.current) return;
    mapRef.current.panTo({ lat: parseFloat(mapCenter.lat), lng: parseFloat(mapCenter.lng) });
    mapRef.current.setZoom(dfZoom);
  }


  // We have to create a mapping of our places to actual Marker objects
  const markerLoadHandler = (marker, place) => {
    return setMarkerMap(prevState => {
      return { ...prevState, [place.Seq]: marker };
    });
  };

  // 點擊Marker時
  const markerClickHandler = (event, place) => {
    // Remember which place was clicked
    setSelectedPlace(place);
    // 更新地圖中心
    handleCenterChanged({ lat: parseFloat(place.Lat), lng: parseFloat(place.Lng) })

    // 判斷是否為群組或單顆電池
    if (place.Type === 'Group') {
      setZoom(12)

      // 群組狀態時
      if (zoom < 11) {
        // if you want to center the selected Marker
        setCenter({ lat: parseFloat(place.Lat), lng: parseFloat(place.Lng) })
        setZoom(11)
      }
    } else {
      // 單顆電池時 infoOpen true 打開詳細清單
      // Required so clicking a 2nd marker works as expected
      if (infoOpen) {
        setInfoOpen(false);
      }
      setInfoOpen(true);
    }
  };

  

  const renderMap = () => {
    return (
      <Fragment>
        {
          data && data.length > 0 ? (
            <GoogleMap
              // Do stuff on map initial laod
              onLoad={loadHandler}
              // Save the current zoom size
              onZoomChanged={handleZoomChanged}
              center={center}
              zoom={zoom}
              options={{
                mapTypeControl: false,
                streetViewControl: false, // 預設 true，地圖與街景切換。
                gestureHandling: "cooperative",
              }}
              mapContainerStyle={{
                height: mapHeight,
                width: "100%",
              }}
            >
              <div className="col-12">
                <div className="map-resetBtn" onClick={handleResetZoom}>{t('1063')}</div>{/* 重置 */}
              </div>
              <Map
                data={data}
                IMPType={IMPType}
                infoOpen={infoOpen}
                markerVisible={markerVisible}
                selectedPlace={selectedPlace}
                markerMap={markerMap}
                markerLoadHandler={markerLoadHandler}
                markerClickHandler={markerClickHandler}
                setInfoOpen={setInfoOpen}
              />
            </GoogleMap>
          ) : <><CardSpinner />loading...</>
        }
      </Fragment>
    )
  };

  return isLoaded ? renderMap() : null;
}

export default WrappedMap;
