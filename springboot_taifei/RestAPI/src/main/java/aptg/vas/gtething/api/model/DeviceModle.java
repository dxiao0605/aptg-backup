package aptg.vas.gtething.api.model;

import java.util.ArrayList;
import java.util.List;

public class DeviceModle {


    //public List<DeviceQuery> DeviceQueryArray =new ArrayList<DeviceQuery>(); ;

    /*
    public class DeviceQueryArray {
        private List<DeviceQuery> devicequeryarray;

        public DeviceQueryArray (List<DeviceQuery> devicequeryarray) {
            this.devicequeryarray = devicequeryarray;
        }
    }*/



     public class DeviceQuery {
        private String device_id;
        private List<Series> series =new ArrayList<Series>();

        public DeviceQuery(String device_id, List<Series> series) {

            this.device_id = device_id;
            this.series.addAll(series);
        }

        public void addSeries(Series ss){

            this.series.add(ss);
        }

    }


     public class Series {
        private long timestamp;
        private Data data;
        public Series(long timestamp, Data data) {

            this.timestamp = timestamp;
            this.data = data;
        }
    }


      public class Data {


        public Data(double waterTemperature, double waterPh, double waterOxygen, double waterOrp,
                    double batterVoltage,double chargingVoltage,int clean_pool) {

            this.water_temperature = waterTemperature;
            this.water_ph = waterPh;
            this.water_oxygen = waterOxygen;
            this.water_orp = waterOrp;
            this.battery_voltage = batterVoltage;
            this.charging_voltage = chargingVoltage;
            this.clean_pool = clean_pool;
        }

        private double water_temperature;

        private double water_ph;

        private double water_oxygen;

        private double water_orp;

        private double battery_voltage;

        private double charging_voltage;

        private int  clean_pool;
    }


}
