package com.yunji.service.impl;

import com.yunji.dao.ArticleMapper;
import com.yunji.dao.ArticleTagMapper;
import com.yunji.model.ArticleTag;
import com.yunji.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liujialiang on 2018/9/27
 */
@Service("articleTagAervice")
public class ArticleTagServiceImpl implements ArticleTagService {
    @Autowired
    ArticleTagMapper articleTagMapper;
    
    @Override
    public int addArticleTag(ArticleTag articleTag) {
        return articleTagMapper.addArticleTag(articleTag);
    }
}
