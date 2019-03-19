package com.yunji.service.impl;

import com.yunji.dao.IdStatusMapper;
import com.yunji.model.IdStatus;
import com.yunji.service.IdStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liujialiang on 2018/9/28
 */
@Service("idStatusService")
public class IdStatusServiceImpl implements IdStatusService {
    @Autowired
    IdStatusMapper idStatusMapper;

    @Override
    public Integer saveStatus(Integer status) {
        IdStatus idStatus = new IdStatus(status);
        idStatusMapper.saveStatus(idStatus);
        return idStatus.getSid();
    }
}
