package docTest;

import aptg.vas.gtething.api.RestSpringbootAppGradle1Application;
import aptg.vas.gtething.api.SwaggerConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import org.asciidoctor.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestSpringbootAppGradle1Application.class, SwaggerConfig.class})
public class toDoc {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void convert() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/apidoc/swagger.json"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();

        Swagger2MarkupConverter.from(json)
                .build()
                .toFile(Paths.get("src/docs/asciidoc/generated"));

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.convertDirectory(
                new GlobDirectoryWalker("*.adoc"), OptionsBuilder
                        .options()
                        .safe(SafeMode.UNSAFE) // CSS
                        .attributes(new Attributes() {{
                            setTableOfContents(Placement.LEFT); // TOC
                        }}));

    }
}