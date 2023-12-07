package aptg.vas.gtething.api.dao;

import aptg.vas.gtething.api.model.DeviceModle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

@Slf4j
@Repository
public class DeviceDAOImpl implements DeviceDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    /*
    public DeviceModle.DeviceQuery find(String DeviceId,String start_date,String end_date) {
        return this.jdbcTemplate.queryForObject("select * from product where id = ?",
                new BeanPropertyRowMapper<DeviceModle.DeviceQuery>(DeviceModle.DeviceQuery.class),id);
    }*/


    public List<DeviceModle.DeviceQuery> find(String DeviceId, String  start_date, String  end_date) {

        log.info("DeviceId="+"-"+DeviceId+"-"+start_date+"-"+end_date);
        String SqlString ="";

        if(end_date==null)
            end_date ="9993139700";

        if(DeviceId.equals("all"))

            SqlString ="SELECT deviceid,timestamp,water_temperature,water_ph,water_oxygen,water_orp,batter_voltage,charging_voltage,clean_pool FROM v_up_data where timestamp between "+ start_date +" and "+ end_date +" order by deviceid,timestamp";

        else
            SqlString ="SELECT deviceid,timestamp,water_temperature,water_ph,water_oxygen,water_orp,batter_voltage,charging_voltage,clean_pool FROM v_up_data where timestamp between "+ start_date +" and "+ end_date +" and deviceid = '"+DeviceId+"'";


        log.info("SqlString="+SqlString);
        List rows = jdbcTemplate.queryForList( SqlString );

       ArrayList<DeviceModle.Series> dmSeries = new ArrayList<DeviceModle.Series>();

        ArrayList<DeviceModle.DeviceQuery> dmdeviceQuerys = new ArrayList<DeviceModle.DeviceQuery>();
        DeviceModle dm= new DeviceModle();
        DeviceModle.DeviceQuery dmDvQuery =null;
        List<DeviceModle.DeviceQuery> dmDvQueryarray =new ArrayList<DeviceModle.DeviceQuery>();


        String didold="aA";
        String isMulti ="N";
        String  did ="";
        Iterator it = rows.iterator();
        while (it.hasNext()) {
            Map QueryMap = (Map) it.next();

            did = QueryMap.get("deviceid").toString();
            Long tt = new Long(QueryMap.get("timestamp").toString());
            Double wt = new Double(QueryMap.get("water_temperature").toString());
            Double wph = new Double(QueryMap.get("water_ph").toString());
            Double wo = new Double(QueryMap.get("water_oxygen").toString());
            Double worp = new Double(QueryMap.get("water_orp").toString());
            Double wbv = new Double(QueryMap.get("batter_voltage").toString());
            Double wcv = new Double(QueryMap.get("charging_voltage").toString());
            int clpool = Integer.parseInt(QueryMap.get("clean_pool").toString());


            DeviceModle.Data dmData = dm.new Data(wt,wph,wo,worp,wbv,wcv,clpool);
            DeviceModle.Series dmSer = dm.new Series(tt,dmData);


            if(!did.equals(didold) && !didold.equals("aA")) {
                dmDvQuery = dm.new DeviceQuery(didold, dmSeries);
                dmDvQueryarray.add(dmDvQuery);
                //dmdeviceQuerys.add(dmDvQuery);
                isMulti ="Y";
                dmSeries.clear();
            }

            didold = did;


            dmSeries.add(dmSer);
            /*
            if(!did.equals(didold)) {
                dmDvQuery = dm.new DeviceQuery(did, dmSeries);
                didold = did;
            }else{

                dmDvQuery.addSeries(dmSer);
            }*/


        }

        //if(isMulti.equals("Y"))
        dmDvQuery = dm.new DeviceQuery(did, dmSeries);
        //dmdeviceQuerys.add(dmDvQuery);


        dmDvQueryarray.add(dmDvQuery);

        return dmDvQueryarray;

    }








}
