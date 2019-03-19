package com.yunji.dao;

import com.yunji.model.IdStatus;
import tk.mybatis.mapper.common.Mapper;

public interface IdStatusMapper extends Mapper<IdStatus> {
    Integer saveStatus(IdStatus idStatus);
}