package com.suza.wasteX.config;

import com.suza.wasteX.DTO.StatusDto.ProjectStatusResponse;
import com.suza.wasteX.statuses.projectStatus.ProjectStatus;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Converter for String -> LocalDate
        Converter<String, LocalDate> toLocalDate = ctx ->
                ctx.getSource() == null ? null : LocalDate.parse(ctx.getSource());

        // Converter for LocalDate -> String (optional, for responses)
        Converter<LocalDate, String> toStringDate = ctx ->
                ctx.getSource() == null ? null : ctx.getSource().toString();

        // Register converters
        mapper.addConverter(toLocalDate);
        mapper.addConverter(toStringDate);

        // Example explicit mapping
        mapper.typeMap(ProjectStatus.class, ProjectStatusResponse.class)
                .addMappings(m -> m.map(ProjectStatus::getStatusDate, ProjectStatusResponse::setStatusDate));

        return mapper;
    }
}
