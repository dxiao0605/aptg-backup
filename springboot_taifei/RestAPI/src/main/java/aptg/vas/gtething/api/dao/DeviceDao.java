package aptg.vas.gtething.api.dao;


import  aptg.vas.gtething.api.model.DeviceModle.*;

import java.sql.Timestamp;
import java.util.List;


public interface DeviceDao {
    public List<DeviceQuery> find(String DeviceId, String start_date, String end_date);
}
