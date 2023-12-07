package aptg.vas.gtething.api.controller;


import aptg.vas.gtething.api.dao.DeviceDao;
import aptg.vas.gtething.api.model.DeviceModle;
import aptg.vas.gtething.api.service.DeviceService;
import aptg.vas.gtething.api.service.ProductService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/")
@Api(tags="API測試", description = "查詢裝置相關操作")
public class DeviceController {


    @Autowired
    private DeviceService deviceService;

    @ApiOperation(value = "取得裝置資訊", notes = "裝置資訊api")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "裝置")})
    @ResponseStatus(HttpStatus.OK)
    //@GetMapping(value = "/device/{device_id}/senson/rawdata", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/device/{device_id}/sensor/rawdata", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String find(@RequestHeader(value="token") String token,@PathVariable("device_id") String deviceid, @RequestParam("start") String  start_date, @RequestParam(value ="end", required=false) String end_date) {

        log.info("裝置資訊api"+"-"+deviceid+"-"+start_date+"-"+end_date+"-"+token);


        if(token.equals("MQnT9H1Cth")) {
            List<DeviceModle.DeviceQuery> retobj = deviceService.find(deviceid, start_date, end_date);

            Gson gson = new Gson();
            String json = gson.toJson(retobj);
            log.info(json + "--" + json.length());

            if (!json.equals("null"))
                return json;
            else
                return "{\"result\":\"No Data\"}";

        }else{

            return "{\"result\":\"Token Error\"}";
        }

    }





}
