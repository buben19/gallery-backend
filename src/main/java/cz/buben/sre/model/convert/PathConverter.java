package cz.buben.sre.model.convert;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.nio.file.Path;
import java.nio.file.Paths;

@Converter
public class PathConverter implements AttributeConverter<Path, String> {

    @Override
    public String convertToDatabaseColumn(Path attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public Path convertToEntityAttribute(String dbData) {
        return StringUtils.isBlank(dbData) ? null : Paths.get(dbData);
    }
}
