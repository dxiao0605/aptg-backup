import React, { useState, useRef, Fragment, useEffect } from "react";

// We will use these things from the lib
// https://react-google-maps-api-docs.netlify.com/
import {
  useLoadScript,
  GoogleMap,
} from "@react-google-maps/api";

/* i18n Functional Components */
import { useTranslation } from 'react-i18next';
// marker&infoWindow component
import { Marker} from "@react-google-maps/api";
import {checkedMarkerType } from './utils';
import { CardSpinner } from '../Spinner';


function LocationMarkerMap({data,ApiKey,mapCenter,dfZoom,mapHeight}) {
    const { t } = useTranslation();
    // The things we need to track in state
    const mapRef = useRef(null);  //實體化map
    // const [selectedPlace, setSelectedPlace] = useState(null); //目前點選的marker
    const [markerMap, setMarkerMap] = useState(); // 實體化map上marker
    const [center, setCenter] = useState(mapCenter); //設定地圖中心點
    const [zoom, setZoom] = useState(dfZoom);  //比例尺



    // 生命週期
    useEffect( () => {
      if (!mapRef.current) return;
      // console.log('>>>>>>>>>>>>>>>mapRef',mapRef,'zoom',zoom)
    },[])



  
    // Load the Google maps scripts
    const { isLoaded } = useLoadScript({
      // Enter your own Google Maps API key
      googleMapsApiKey: ApiKey,
    });
  
  
    // Iterate data to size, center, and zoom map to contain all markers
    // const fitBounds = map => {
    //   const bounds = new window.google.maps.LatLngBounds();
    //   data.map(place => {
    //     bounds.extend({ lat: place.Lat, lng: place.Lng });
    //     return place.Seq;
    //   });
    //   map.fitBounds(bounds);
    // };
  
    // 載入座標點&實體化map
    const loadHandler = map => {
      if (map === undefined) { return; }
      // Store a reference to the google map instance in state
      mapRef.current = map;
      // // Fit map bounds to contain all markers
      // fitBounds(map);
      // mapRef.current.setZoom(17);
    };

    // 變更中心點
    // const handleCenterChanged = (newPos) => {
    //   // if (!mapRef.current) return;
    //   setCenter(newPos)
    // }

    // 變更比例尺
    const handleZoomChanged = () => {
      if (!mapRef.current) return;
      const newZoom = mapRef.current.getZoom();
      setZoom(newZoom);
    }
    // 還原比例尺 zoom 11
    const handleResetZoom = () => {
      if (!mapRef.current) return;
      setCenter({ lat:mapCenter.lat, lng:mapCenter.lng })
      mapRef.current.panTo({ lat:mapCenter.lat, lng:mapCenter.lng });
      mapRef.current.setZoom(17);
    }

  
    // We have to create a mapping of our places to actual Marker objects
    const markerLoadHandler = (marker, place) => {
      console.log(markerMap)
      return setMarkerMap(prevState => {
        return { ...prevState, [place.Seq]: marker };
      });
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
                // scrollwheel: false,       // ( true/false ) 是否支援滑鼠滾輪，預設 true 可用滑鼠滾輪縮放地圖
                streetViewControl: false, // 預設 true，地圖與街景切換。
                gestureHandling: "cooperative",
                // zoomControl: false,
                // fullscreenControl: false,
              }}
              mapContainerStyle={{
                height: mapHeight,  // 54vh
                width: "100%",
              }}
            >
              <div className="col-12">
                <div  className="map-resetBtn" onClick={handleResetZoom}>{t('1063')}</div>{/* 重置 */}
              </div>
                {data.map((place,idx) => (
                  <Fragment key={idx}>
                    <Marker
                      position={{ lat: place.Lat, lng: place.Lng }}
                      onLoad={(marker) => markerLoadHandler(marker, place)}
                      icon={{
                        url: checkedMarkerType(place.EventTypeCode).toString(),
                        scaledSize: { width: 35, height: 45 },
                      }}
                    />
                  </Fragment>
                ))}
            </GoogleMap>
            ) : <><CardSpinner />loading...</>
          }
        </Fragment>
      )
    };
  
    return isLoaded ? renderMap() : null;
}

export default LocationMarkerMap;
