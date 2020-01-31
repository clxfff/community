package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author clx
 */
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{

    @Override
    public String select() {
        return "Hibernate";
    }
}
