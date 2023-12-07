package docTest;

import aptg.vas.gtething.api.RestSpringbootAppGradle1Application;
import aptg.vas.gtething.api.SwaggerConfig;

//import io.github.robwin.markup.builder.MarkupLanguage;
//import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
//import io.github.swagger2markup.Swagger2MarkupConverter;
//import io.github.swagger2markup.markup.builder.MarkupLanguage;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {RestSpringbootAppGradle1Application.class, SwaggerConfig.class})
@SpringBootTest(classes = {RestSpringbootAppGradle1Application.class, SwaggerConfig.class})
//@WebIntegrationTest
@WebAppConfiguration
public class Swagger2MarkupTest {

    /*
    @Test
    public void convertRemoteSwaggerToAsciiDoc() throws IOException {
        // Remote Swagger source
        Swagger2MarkupConverter.from(new URL("http://localhost:8080/waterfish/apidoc/swagger.json")).build()
                .toFolder(Paths.get("src/docs/asciidoc/generated"));


        String[] files = new File("src/docs/asciidoc/generated").list();
        System.out.println(files.length);

    }



    @Test
    public void convertRemoteSwaggerToMarkdown() throws IOException {
        Swagger2MarkupConverter.from("http://localhost:8080/waterfish/apidoc/swagger.json")
                .withMarkupLanguage(MarkupLanguage.MARKDOWN).build()
                .intoFolder("src/docs/asciidoc/generated");

        // Then validate that three Markdown files have been created
        String[] files = new File("src/docs/asciidoc/generated").list();
        System.out.println(files.length);
    }

*/
}
