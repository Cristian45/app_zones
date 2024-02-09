package com.personal.backzone.service.mapper;

import com.personal.backzone.domain.Pest;
import com.personal.backzone.service.dto.PestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pest} and its DTO {@link PestDTO}.
 */
@Mapper(componentModel = "spring")
public interface PestMapper extends EntityMapper<PestDTO, Pest> {}
