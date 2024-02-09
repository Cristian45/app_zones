package com.personal.backzone.service.mapper;

import com.personal.backzone.domain.Pest;
import com.personal.backzone.domain.Zone;
import com.personal.backzone.domain.ZonePest;
import com.personal.backzone.service.dto.PestDTO;
import com.personal.backzone.service.dto.ZoneDTO;
import com.personal.backzone.service.dto.ZonePestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ZonePest} and its DTO {@link ZonePestDTO}.
 */
@Mapper(componentModel = "spring")
public interface ZonePestMapper extends EntityMapper<ZonePestDTO, ZonePest> {
    @Mapping(target = "zoneId", source = "zoneId", qualifiedByName = "zoneId")
    @Mapping(target = "pestId", source = "pestId", qualifiedByName = "pestId")
    ZonePestDTO toDto(ZonePest s);

    @Named("zoneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ZoneDTO toDtoZoneId(Zone zone);

    @Named("pestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PestDTO toDtoPestId(Pest pest);
}
