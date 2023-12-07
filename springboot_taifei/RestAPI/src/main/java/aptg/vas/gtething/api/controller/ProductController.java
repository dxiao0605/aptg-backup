package aptg.vas.gtething.api.controller;

import java.util.List;


import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import aptg.vas.gtething.api.model.Product;
import aptg.vas.gtething.api.service.ProductService;

//http://localhost:8080/waterfish/api/product
//http://localhost:8080/waterfish/apidoc/index.html
//http://localhost:8080/waterfish/swagger-ui.html aaaa22223333

@Slf4j
@RestController
@RequestMapping(value = "/api")
@Api(tags="API測試", description = "查詢裝置相關操作")
public class ProductController {


	@Autowired
	private ProductService productService;
	

	@ApiOperation(value="所有裝置列表", notes="裝置列表api")
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Product> getAllproduct(){
		log.info("getAllproduct");
		return productService.findAll();
	}


	@ApiOperation(value = "取得裝置資訊", notes = "裝置資訊api")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "裝置")})
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = "/product/{deviceid}/senson/{sensorid}/rawdata", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product getProduct(@PathVariable("deviceid") int deviceid,@PathVariable("sensorid") int sensorid,@RequestParam("start") String start_date,@RequestParam("end") String end_date) {

		log.info("裝置資訊api"+"-"+deviceid+"-"+sensorid+"-"+start_date+"-"+end_date);
		return productService.find(deviceid);
		
	}

}
