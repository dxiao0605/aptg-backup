package aptg.vas.gtething.api;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2
@Import(SpringDataRestConfiguration.class)
public class SwaggerConfig {

    //String basePackage ="aptg.vas.gtething.api.controller";

    String basePackage ="aptg.vas.gtething.api.controller";

    @Bean
    public Docket createRestApi() {

/*

        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("token").description("證證")//name表示名称，description表示描述
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).defaultValue("12345").build();//required表示是否必填，defaultvalue表示默认值
        pars.add(ticketPar.build());//添加完此处一定要把下边的带***的也加上否则不生效

*/

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()  //RequestHandlerSelectors.any()，所有的路劲都去扫描
                //这个扫描包的意思一样，固定路劲就去相应的路劲扫描
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
               // .globalOperationParameters(pars)
               // .apiInfo(apiInfo());
    }

    //就是对生成的api文档的一个描述

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("亞太電信Gt-Ething Open Data 平台")
                .description("專案")
               .termsOfServiceUrl("相关url")
               .contact("亞太電信-資訊中心-加值服務系統部-系統架構暨工程組")
               .version("版本：1.0")
                .build();
    }



}
