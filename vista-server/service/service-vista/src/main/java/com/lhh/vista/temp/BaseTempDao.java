package com.lhh.vista.temp;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.base.Strings;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by soap on 2016/12/10.
 */
public class BaseTempDao {
    @Resource(name = "tempSqlSession")
    protected SqlSession tempSqlSession; // 注入一个session

    protected <T> PagerResponse<T> getPagerByCmd(String queryCmd, PagerRequest pager, Object param) {
        //解决排序问题
        MappedStatement ms = tempSqlSession.getConfiguration().getMappedStatement(queryCmd);
        if (ms == null) {
            return new PagerResponse<T>();
        }
        PageBounds pageBounds = null;
        if (Strings.isNullOrEmpty(pager.getOrderEx())) {
            boolean order = false;
            if (!Strings.isNullOrEmpty(pager.getSort()) && !Strings.isNullOrEmpty(pager.getOrder())) {
                List<ResultMap> rms = ms.getResultMaps();
                for (ResultMap map : rms) {
                    for (ResultMapping mp : map.getPropertyResultMappings()) {
                        if (pager.getSort().equals(mp.getProperty())) {
                            pager.setSort(mp.getColumn());
                            order = true;
                            break;
                        }
                    }
                    if (order) {
                        break;
                    }
                }
            }

            if (order) {
                pageBounds = new PageBounds(pager.getPage(), pager.getRows(), Order.formString(pager.getSort() + "." + pager.getOrder()));
            } else {
                pageBounds = new PageBounds(pager.getPage(), pager.getRows());
            }
        } else {
            pageBounds = new PageBounds(pager.getPage(), pager.getRows(), Order.formString(pager.getOrderEx()));
        }

        List<T> list = tempSqlSession.selectList(queryCmd, param, pageBounds);
        PageList<T> pageList = (PageList<T>) list;
        PagerResponse<T> pagerResponse = new PagerResponse<T>(pageList.getPaginator().getTotalCount(), list);
        return pagerResponse;
    }
}
