package com.lhh.vista.service.service;

import com.lhh.vista.service.dao.SearchValueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liu on 2016/12/29.
 */
@Service
public class SearchValueServiceImpl implements SearchValueService {
    @Autowired
    private SearchValueDao searchValueDao;

    @Override
    public void addSearchValue(String value) {
        if (searchValueDao.getCount(value) == 0) {
            searchValueDao.create(value);
        } else {
            searchValueDao.addCount(value);
        }
    }

    @Override
    public List<String> getHots() {
        return searchValueDao.getHots();
    }
}
