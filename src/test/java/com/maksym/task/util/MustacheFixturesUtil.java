package com.maksym.task.util;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MustacheFixturesUtil {

    @SneakyThrows
    public static String compileTemplate(String fixturePath, Object data) {
        String templateContent = IOUtils.toString(MustacheFixturesUtil.class.getResourceAsStream(fixturePath), StandardCharsets.UTF_8);
        Template template = Mustache.compiler().compile(templateContent);
        return template.execute(data);
    }
}
