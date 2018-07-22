/**
 * @{#} ResourceDTO.java Created on 2016-09-22 16:04:07
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.service.sm.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author masb
 * 
 *         对应实体类(Resource)
 */
public class MenuSortDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<MenuSrot> menuSortlist = new ArrayList<MenuSrot>();

    public List<MenuSrot> getMenuSortlist() {
        return menuSortlist;
    }

    public void setMenuSortlist(List<MenuSrot> menuSortlist) {
        this.menuSortlist = menuSortlist;
    }

    public class MenuSrot {
        /**
         * 菜单排序
         */
        private String sort;

        private String resId;

        public MenuSrot() {

        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getResId() {
            return resId;
        }

        public void setResId(String resId) {
            this.resId = resId;
        }
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}