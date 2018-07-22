/*
 * AppAccessService.java Created on 2017年3月25日 下午2:25:45
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wst.sys.service;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.wst.service.sys.dto.AppAccessDTO;
import com.wst.service.sys.dto.AppIpDTO;
import com.wst.service.sys.service.AppAccessService;
import com.wst.sys.dao.AppAccessDao;

/**
 * 第三方应用接入Service
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class AppAccessServiceImpl implements AppAccessService {

    @Resource
    private AppAccessDao AppAccessDao;

    @Override
    public AppAccessDTO getByAppId(String appId) throws Exception {
        return AppAccessDao.getByAppId(appId);
    }

    @Override
    public List<AppIpDTO> findWhiteIpList(long accsId) throws Exception {
        return AppAccessDao.findWhiteIpList(accsId);
    }

/*    public ResultDTO<String> saveorUpdate(AppAccessDTO appAccessDTO) throws Exception {
        ResultDTO<String> result = new ResultDTO<String>();
        if (appAccessDTO.getAccsId() == 0) {
            AppAccessDTO oldAppAccess = AppAccessDao.getAllByAppId(appAccessDTO.getAppId());
            if (null == oldAppAccess) {
                String appSecret = MD5Util
                        .MD5(BackConstant.KEY + System.currentTimeMillis() + appAccessDTO.getAccsId()).substring(8, 24);
                appAccessDTO.setAppSecret(appSecret);
                AppAccessDao.saveAccess(appAccessDTO);
                return result.set(ResultCodeEnum.SUCCESS, "保存成功 ！");
            } else {
                return result.set(ResultCodeEnum.ERROR_HANDLE, "应用Id重复！请重新输入");
            }
        } else {
            AppAccessDao.updateAppAccess(appAccessDTO);
            return result.set(ResultCodeEnum.SUCCESS, "修改成功");
        }
    }

    @Override
    public ResultDTO<String> upAppSecret(long accsId, long opId) throws Exception {
        String appSecret = MD5Util.MD5(BackConstant.KEY + System.currentTimeMillis() + accsId).substring(8, 24);
        AppAccessDao.upAppSecret(accsId, appSecret, opId);
        return new ResultDTO<String>().set(ResultCodeEnum.SUCCESS, "变更成功！");
    }*/

    @Override
    public Page<AppAccessDTO> findPage(Page<AppAccessDTO> pageParam, AppAccessDTO appAccess) throws Exception {
        return AppAccessDao.findPage(pageParam, appAccess);
    }

    @Override
    public AppAccessDTO getAllById(long accsId) throws Exception {
        return AppAccessDao.getAllById(accsId);
    }

    @Override
    public AppAccessDTO getAllByAppId(String appId) throws Exception {
        return AppAccessDao.getAllByAppId(appId);
    }
}
