package aptg.vas.gtething.api.service;

import aptg.vas.gtething.api.model.DeviceModle;
import aptg.vas.gtething.api.model.Product;

import java.sql.Timestamp;
import java.util.List;

public interface DeviceService {
    public List<DeviceModle.DeviceQuery> find(String DeviceId, String start_date, String  end_date);
}
