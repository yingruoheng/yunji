package com.yunji.dao;

import com.yunji.model.ArticleTag;
import tk.mybatis.mapper.common.Mapper;

public interface ArticleTagMapper extends Mapper<ArticleTag> {
    int addArticleTag(ArticleTag articleTag);
}