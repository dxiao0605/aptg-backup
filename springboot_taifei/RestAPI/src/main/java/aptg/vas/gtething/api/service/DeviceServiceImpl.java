package aptg.vas.gtething.api.service;

import aptg.vas.gtething.api.dao.DeviceDao;
import aptg.vas.gtething.api.dao.ProductDAO;
import aptg.vas.gtething.api.model.DeviceModle;
import aptg.vas.gtething.api.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {


    @Autowired
    private DeviceDao deviceDao;

    @Override
    public List<DeviceModle.DeviceQuery> find(String DeviceId, String  start_date, String end_date) {
        // TODO Auto-generated method stub
        return deviceDao.find(DeviceId, start_date, end_date);
    }

}
