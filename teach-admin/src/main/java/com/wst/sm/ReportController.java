/*
 * ReportController.java Created on 2016年10月9日 下午2:44:00
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
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
package com.wst.sm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author <a href="mailto:liul@hiwitech.com">Louis Liu</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin")
public class ReportController {

/*    private static HiwiLog log = HiwiLogFactory.getLogger(ReportController.class);
    
    @Value("${report.server.url}")
    private String reportServer;
    
    @RequestMapping("report")
    public void report(HttpServletRequest request,HttpServletResponse response,String pageId)throws Exception{
        log.debug("访问报表:"+reportServer+"   "+ request.getRequestURL());
        response.sendRedirect(reportServer+"?PAGEID="+request.getParameter("PAGEID"));
        
    }*/
}
